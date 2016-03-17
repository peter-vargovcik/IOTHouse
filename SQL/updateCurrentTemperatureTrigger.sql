delimiter //
CREATE TRIGGER updateCurrentTemperature AFTER INSERT ON IOTHouse.temperatures
    FOR EACH ROW
    BEGIN
        IF NEW.temperatureZone in (
            select A.temperatureZone
            From IOTHouse.CurrentTemperatures A  
            where (NEW.temperatureZone = A.temperatureZone)
        ) THEN 
           UPDATE IOTHouse.CurrentTemperatures SET temperature = NEW.temperature WHERE NEW.temperatureZone = temperatureZone;
		ELSE
			INSERT INTO `IOTHouse`.`CurrentTemperatures` (`IOTNodeID`,`temperatureZone`,`temperature`)
			VALUES (NEW.IOTNodeID,NEW.temperatureZone,NEW.temperature);
        END IF;
    END;
//
delimiter ; 