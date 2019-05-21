package rest.model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;

@XmlRootElement(name = "filme")
@XmlAccessorType(XmlAccessType.FIELD)

public class Filmes {

private int id;
private String nomefilme;
private String sinopse;

public Filmes() {
	
}

public Filmes(int id, String nomefilme, String sinopse) {
	super();
	this.id = id;
	this.nomefilme = nomefilme;
	this.sinopse = sinopse;
}

public int getId() {
	return id;
}

public void setId(int id) {
	this.id = id;
}

public String getNomefilme() {
	return nomefilme;
}

public void setNomefilme(String nomefilme) {
	this.nomefilme = nomefilme;
}

public String getSinopse() {
	return sinopse;
}

public void setSinopse(String sinopse) {
	this.sinopse = sinopse;
}

@Override
public String toString() {
	return "Filmes [id=" + id + ", nomefilme=" + nomefilme + ", sinopse=" + sinopse + "]";
}



}
