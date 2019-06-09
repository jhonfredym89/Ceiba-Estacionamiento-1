package co.com.ceiba.adn.parking.service.application.query.handle;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.com.ceiba.adn.parking.service.domain.model.Ingreso;
import co.com.ceiba.adn.parking.service.domain.query.port.QueryPortIngreso;

@Component
public class QueryHandleIngresoFindAll {

	@Autowired
	private QueryPortIngreso queryPortIngreso;
	
	public List<Ingreso> handle() {
		return this.queryPortIngreso.findAll();
	}
}
