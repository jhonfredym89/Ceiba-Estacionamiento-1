package co.com.ceiba.adn.parking.service.application.command.handle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.com.ceiba.adn.parking.common.application.CommandResponse;
import co.com.ceiba.adn.parking.common.application.handle.CommandHandleResponse;
import co.com.ceiba.adn.parking.service.application.command.CommandIngreso;
import co.com.ceiba.adn.parking.service.application.command.factory.FactoryIngreso;
import co.com.ceiba.adn.parking.service.domain.command.port.CommandPortIngreso;
import co.com.ceiba.adn.parking.service.domain.model.Ingreso;

@Component
public class CommandHandleCreateIngreso implements CommandHandleResponse<CommandIngreso, CommandResponse<Long>> {

	@Autowired
	private CommandPortIngreso commandPortIngreso;
	
	@Override
	public CommandResponse<Long> exec(CommandIngreso commandIngreso) {
		Ingreso ingreso = commandPortIngreso.insertIngreso(FactoryIngreso.getInstance().create(commandIngreso));
		return new CommandResponse<>(ingreso.getId());
	}

}
