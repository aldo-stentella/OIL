package it.cnr.helpdesk.rest.model;

import java.io.Serializable;

/**
 * Wrapper per Problema Utente Esterno
 * 
 * @author Aurelio D'Amico
 * @version 1.0 [Apr 18, 2016]
 */
public class ExternalProblem implements Serializable {
	
	private static final long serialVersionUID = 1L;
	public static final int PRIORITY = 2;
	public static final String[] NEW = 
	{"firstName","familyName","email","titolo","descrizione","categoria","categoriaDescrizione","confirmRequested"};
	public static final String[] STT = {"idSegnalazione","nota","stato","login"};
	public static final String[] ATT = {"idSegnalazione","allegato","descrizione"};
	public static final String[] CTG = {"idSegnalazione","categoria","categoriaDescrizione","login"};
	
	public static final String ALLEGATO = "allegato";

	// user
    private String firstName;
    private String familyName;
    private String email;
    private String login;

    // problem
	private Long idSegnalazione;
    private String titolo;
    private String descrizione;
    private Integer categoria;
    private String categoriaDescrizione;
    private String confirmRequested;
    
    // note
    private String nota;
    private Integer stato;
    
    // allegato bas64
    private String allegato;

	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getFamilyName() {
		return familyName;
	}
	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public Long getIdSegnalazione() {
		return idSegnalazione;
	}
	public void setIdSegnalazione(Long idSegnalazione) {
		this.idSegnalazione = idSegnalazione;
	}
	public String getTitolo() {
		return titolo;
	}
	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public Integer getCategoria() {
		return categoria;
	}
	public void setCategoria(Integer categoria) {
		this.categoria = categoria;
	}
	public String getCategoriaDescrizione() {
		return categoriaDescrizione;
	}
	public void setCategoriaDescrizione(String categoriaDescrizione) {
		this.categoriaDescrizione = categoriaDescrizione;
	}
	public String getNota() {
		return nota;
	}
	public void setNota(String nota) {
		this.nota = nota;
	}
	public Integer getStato() {
		return stato;
	}
	public void setStato(Integer stato) {
		this.stato = stato;
	}
	public String getAllegato() {
		return allegato;
	}
	public void setAllegato(String allegato) {
		this.allegato = allegato;
	}
	public String getConfirmRequested() {
		return confirmRequested;
	}
	public void setConfirmRequested(String confirmRequested) {
		this.confirmRequested = confirmRequested;
	}
}
