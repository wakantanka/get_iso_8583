package com.wirecard.acqp.twi;

/**
 * @author jan.wahler
 * Copyright Wirecard AG (c) 2014. All rights reserved.
 * @link http://en.wikipedia.org/wiki/Card_schemes
 */
public enum CardScheme {
VISA("base1"), MASTERCARD("basic"), JCB("tbd");

private String path;

CardScheme(String path){
	this.path=path;
}
public String getPath() {
    return "src/main/resources/" + path + ".xml";
}

}

