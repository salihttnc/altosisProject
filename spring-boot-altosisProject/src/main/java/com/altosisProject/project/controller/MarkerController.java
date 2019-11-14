package com.altosisProject.project.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.altosisProject.project.model.Marker;
import com.altosisProject.project.repository.MarkerRespository;
import com.datastax.driver.core.BoundStatement;

@RestController
public class MarkerController {

	@Autowired
	MarkerRespository markerRepository;

	@GetMapping(value = "/healthcheck", produces = "application/json; charset=utf-8")
	public String getHealthCheck() {
		return "{ \"App is up and running\" : true }";
	}

	@GetMapping("/")
	public List<Marker> getEmployees() {
		List<Marker> markerList = new ArrayList<Marker>();
		markerRepository.findAll().forEach(markerList::add);
		return markerList;

	}

	@GetMapping("/{distance}/{lat}/{lng}")
	public List<Marker> getMarkers(@PathVariable("distance") double distance, @PathVariable("lat") double lat,
			@PathVariable("lng") double lng) {
		DecimalFormat df = new DecimalFormat("#.#######");
		distance = distance / 6371;
		String dx4 = df.format(distance);
		distance = Double.valueOf(dx4);
		double lonmin;
		double lonmax;
		System.out.println("distance degeri:" + distance);

		double latmin = lat * Math.PI / 180 - distance;
		double latmax = lat * Math.PI / 180 + distance;
		System.out.println("Degerler1 =" + latmin + " " + latmax);
		double latT = Math.asin(Math.sin(lat) / Math.cos(distance));
		System.out.println("Degerler2 =" + latT);
		double deltalon = Math.asin(Math.sin(distance) / Math.cos(lat));
		lng = lng * Math.PI / 180;
		System.out.println("Degerler3 =" + lng + " " + deltalon);
		lonmin = Math.abs(lng - deltalon);
		lonmax = lng + deltalon;
		latmin = latmin * 180 / Math.PI;
		latmax = latmax * 180 / Math.PI;
		lonmin = lonmin * 180 / Math.PI;
		lonmax = lonmax * 180 / Math.PI;
		String dx = df.format(latmin);
		latmin = Double.valueOf(dx);

		String dx1 = df.format(latmax);
		latmax = Double.valueOf(dx1);

		String dx2 = df.format(lonmin);
		lonmin = Double.valueOf(dx2);

		String dx3 = df.format(lonmax);
		lonmax = Double.valueOf(dx3);
		System.out.println("Degerler4 =" + latmin + " " + latmax + " " + lonmin + " " + lonmax);
		List<Marker> emp = markerRepository.findByLatAndLngGreaterThan(latmin, latmax, lonmax, lonmin);

		long startTime = System.currentTimeMillis();

		return emp;
	}

	@PostConstruct
	public void saveMarker() {

		List<Marker> users = new ArrayList<>();
		for (int i = 0; i < 0; i++) {
			Random r = new Random();
			double randomValueLng = 24.156340 + (46.129262 - 24.156340) * r.nextDouble();
			Random r1 = new Random();
			double randomValueLat = 33.376978 + (45.244224 - 33.376978) * r1.nextDouble();
			DecimalFormat df = new DecimalFormat("#.#######");

			String dx = df.format(randomValueLng);

			double lng = Double.valueOf(dx);
			String dx1 = df.format(randomValueLat);

			double lat = Double.valueOf(dx1);
			System.out.print(lat + "  " + lng + "" + i + "\n");

			String id = String.valueOf(i + 1);
			users.add(new Marker(id, lat, lng));

		}
		markerRepository.saveAll(users);
	}

}
