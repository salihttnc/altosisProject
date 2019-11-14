package com.altosisProject.project.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.altosisProject.project.model.Marker;

public interface MarkerRespository extends  CrudRepository<Marker, String>{
	 @Query("SELECT * from marker where  lat>?0 AND lat<?1 AND lng>?2 AND lng<?3 ALLOW FILTERING")
	 public List<Marker> findByLatAndLngGreaterThan(double latmin,double latmax,double lonmax,double lonmin);
		 
	

}
