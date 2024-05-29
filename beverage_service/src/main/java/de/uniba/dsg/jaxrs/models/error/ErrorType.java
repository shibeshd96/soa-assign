package de.uniba.dsg.jaxrs.models.error;

import jakarta.xml.bind.annotation.*;

@XmlEnum(String.class)
public enum ErrorType {
	INVALID_PARAMETER, ORDER_ALREADY_PROCESSED, CANNOT_DELETE, NOT_FOUND
}
