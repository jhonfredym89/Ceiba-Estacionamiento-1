package co.com.ceiba.adn.parking.domain.command.service;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import org.junit.Test;
import org.mockito.Mockito;

import co.com.ceiba.adn.TestBase;
import co.com.ceiba.adn.parking.domain.command.repository.CommandRepositoryEntry;
import co.com.ceiba.adn.parking.domain.command.testdatabuilder.EntryTestDataBuilder;
import co.com.ceiba.adn.parking.domain.exception.ExceptionEntryExist;
import co.com.ceiba.adn.parking.domain.exception.ExceptionEntryNotAllowed;
import co.com.ceiba.adn.parking.domain.model.Entry;
import co.com.ceiba.adn.parking.domain.query.repository.QueryRepositoryEntry;

public class CommandServiceCreateEntryTest {

	@Test
	public void validateInsertEntryWithLicencePlateValueA() {
		// Arrange
		String messageLicencePlateNotAllowed = "Ingreso no permitido, el tipo de placa indicado solo tiene permitido el ingreso los d�as domingo y lunes.";
		int limitCount = 20;
		
		Calendar date = Calendar.getInstance();
		date.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY); // Date greater than MONDAY

		EntryTestDataBuilder entryTestDataBuilder = new EntryTestDataBuilder();
		entryTestDataBuilder.withLicencePlate("ASD123").withEntryTime(date);
		Entry entry = entryTestDataBuilder.build();

		QueryRepositoryEntry queryPortEntry = Mockito.mock(QueryRepositoryEntry.class);
		Mockito.when(queryPortEntry.countByVehicleType(Mockito.anyString())).thenReturn(limitCount);
		Mockito.when(queryPortEntry.findByLicencePlate(Mockito.anyString())).thenReturn(null);

		CommandRepositoryEntry commandPortEntry = Mockito.mock(CommandRepositoryEntry.class);
		Mockito.when(commandPortEntry.insertEntry(entry)).thenReturn(entry);

		CommandServiceCreateEntry commandServiceCreateEntry = new CommandServiceCreateEntry(queryPortEntry,
				commandPortEntry);

		// Act - Assert
		TestBase.assertThrows(() -> commandServiceCreateEntry.exec(entry), ExceptionEntryNotAllowed.class,
				messageLicencePlateNotAllowed);
	}

	@Test
	public void validateInsertEntryWithCarLimitReached() {

		// Arrange
		String messageVehicleLimitReached = "Ingreso no permitido, no hay mas cupo en el parqueadero.";
		String fieldVehicleType = "CAR";
		String fieldLicencePlate = "DFR345";
		int limitCarCount = 20;

		EntryTestDataBuilder entryTestDataBuilder = new EntryTestDataBuilder();
		entryTestDataBuilder.withLicencePlate(fieldLicencePlate).withVehicleType(fieldVehicleType);
		Entry entry = entryTestDataBuilder.build();

		QueryRepositoryEntry queryPortEntry = Mockito.mock(QueryRepositoryEntry.class);
		Mockito.when(queryPortEntry.countByVehicleType(Mockito.anyString())).thenReturn(limitCarCount);
		Mockito.when(queryPortEntry.findByLicencePlate(Mockito.anyString())).thenReturn(null);

		CommandRepositoryEntry commandPortEntry = Mockito.mock(CommandRepositoryEntry.class);
		Mockito.when(commandPortEntry.insertEntry(entry)).thenReturn(entry);

		CommandServiceCreateEntry commandServiceCreateEntry = new CommandServiceCreateEntry(queryPortEntry,
				commandPortEntry);

		// Act - Assert
		TestBase.assertThrows(() -> commandServiceCreateEntry.exec(entry), ExceptionEntryNotAllowed.class,
				messageVehicleLimitReached);

	}


	@Test
	public void validateInsertEntryWithMotorcycleLimitReached() {

		// Arrange
		String messageVehicleLimitReached = "Ingreso no permitido, no hay mas cupo en el parqueadero.";
		String fieldVehicleType = "MOTORCYCLE";
		String fieldLicencePlate = "HFR345";
		String engineDisplacement = "100";
		int limitMotorcycleCount = 10;

		EntryTestDataBuilder entryTestDataBuilder = new EntryTestDataBuilder();
		entryTestDataBuilder.withLicencePlate(fieldLicencePlate).withVehicleType(fieldVehicleType).withEngineDisplacement(engineDisplacement);
		Entry entry = entryTestDataBuilder.build();

		QueryRepositoryEntry queryPortEntry = Mockito.mock(QueryRepositoryEntry.class);
		Mockito.when(queryPortEntry.countByVehicleType(Mockito.anyString())).thenReturn(limitMotorcycleCount);
		Mockito.when(queryPortEntry.findByLicencePlate(Mockito.anyString())).thenReturn(null);

		CommandRepositoryEntry commandPortEntry = Mockito.mock(CommandRepositoryEntry.class);
		Mockito.when(commandPortEntry.insertEntry(entry)).thenReturn(entry);

		CommandServiceCreateEntry commandServiceCreateEntry = new CommandServiceCreateEntry(queryPortEntry,
				commandPortEntry);

		// Act - Assert
		TestBase.assertThrows(() -> commandServiceCreateEntry.exec(entry), ExceptionEntryNotAllowed.class,
				messageVehicleLimitReached);

	}
	
	@Test
	public void validateInsertEntryWithLicencePlateDuplicated() {
		
		// Arrange
		EntryTestDataBuilder entryTestDataBuilder = new EntryTestDataBuilder();
		Entry entry = entryTestDataBuilder.build();
		String messageEntryFound = "Ya se encuentra un vehiculo en el parqueadero con la placa proporcionada.";

		QueryRepositoryEntry queryRepositoryEntry = Mockito.mock(QueryRepositoryEntry.class);
		Mockito.when(queryRepositoryEntry.findByLicencePlate(Mockito.anyString())).thenReturn(entry);

		CommandRepositoryEntry commandRepositoryEntry = Mockito.mock(CommandRepositoryEntry.class);
		Mockito.when(commandRepositoryEntry.insertEntry(entry)).thenReturn(entry);
		
		CommandServiceCreateEntry commandServiceCreateEntry = new CommandServiceCreateEntry(queryRepositoryEntry,
				commandRepositoryEntry);
		
		// Act - Assert
		TestBase.assertThrows(() -> commandServiceCreateEntry.exec(entry), ExceptionEntryExist.class,
				messageEntryFound);
	}


	@Test
	public void doCorrentInsert() {
		
		// Arrange
		int limitMotorcycleCount = 10;
		EntryTestDataBuilder entryTestDataBuilder = new EntryTestDataBuilder();
		entryTestDataBuilder.withLicencePlate("DFR345");
		Entry entry = entryTestDataBuilder.build();
				
		QueryRepositoryEntry queryPortEntry = Mockito.mock(QueryRepositoryEntry.class);
		Mockito.when(queryPortEntry.countByVehicleType(Mockito.anyString())).thenReturn(limitMotorcycleCount);

		CommandRepositoryEntry commandPortEntry = Mockito.mock(CommandRepositoryEntry.class);
		Mockito.when(commandPortEntry.insertEntry(entry)).thenReturn(entry);
		
		
		CommandServiceCreateEntry commandServiceCreateEntry = new CommandServiceCreateEntry(queryPortEntry,
				commandPortEntry);
		
		// Act
		Long idCreated = commandServiceCreateEntry.exec(entry);
		
		// Assert
		assertEquals(idCreated, entry.getId());
		
	}

}
