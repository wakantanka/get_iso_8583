package de.wirecard.acqp.twi;

/**
 * @author jan.wahler
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

