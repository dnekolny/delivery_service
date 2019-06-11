package com.example.delivery_service.model.Entity;

import com.example.delivery_service.model.GoogleMapsApi;
import com.example.delivery_service.services.StateService;
import com.google.maps.errors.ApiException;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Date;

@Entity
public class Address {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private Date createDate;

    private Date updateDate;

    @NotNull
    @ManyToOne(targetEntity = State.class, fetch = FetchType.EAGER)
    private State state;

    @NotBlank
    private String street;

    @NotBlank
    private String city;

    @NotBlank
    private String zip;

    @OneToOne(targetEntity = LatLng.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private LatLng latLng;

    public Address(){}

    public Address(Date createDate, Date updateDate, @NotNull State state, @NotBlank String street, @NotBlank String city, @NotBlank String zip) {
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.state = state;
        this.street = street;
        this.city = city;
        this.zip = zip;
    }

    public boolean isEmpty(){
        return isEmpty(false);
    }

    //includShortcut = false -> adresa se považuje za prázdnou i když obsahuje shortcut
    //                 true -> adresa je prázdná pouze když jsou všechny parametry null nebo prázdné
    public boolean isEmpty(boolean includShortcut){
        if (includShortcut)
            return (state == null && street.isEmpty() && city.isEmpty() && zip.isEmpty());
        else
            return ((state == null || (state.getId() == null && (state.getName() == null || state.getName().isEmpty()))) && street.isEmpty() && city.isEmpty() && zip.isEmpty());
    }

    public boolean isValid(){
        return (state != null && !state.getShortcut().isEmpty() && !street.isEmpty() && !city.isEmpty() && !zip.isEmpty());
    }

    public void fillStateFromShortcut(StateService stateService){
        if(getState() != null && !getState().getShortcut().equals("")) {
            setState(stateService.getStateByShortcut(getState().getShortcut()).orElse(null));
        }
    }

    public void setCreateAndUpdateDates(Address address){
        if(address != null)
            setCreateDate(address.getCreateDate());
        else
            setCreateDate(new Date());
        setUpdateDate(new Date());
    }

    public String getFormatAddress(){
        return getFormatAddress(true);
    }

    public String getFormatAddress(boolean withState){
        if(withState)
            //Valdštejnovo nám. 44, Staré Město, 506 01 Jičín, Česko
            return getStreet() + ", " + getZip() + " " + getCity() + ", " + getState().getName();
        else
            return getStreet() + ", " + getZip() + " " + getCity();
    }

    public String getShortAddress(){
        return getStreet() + ", " + getCity();
    }

    public static Address getOfficeAddress(StateService stateService){
        //Ve žlíbku 1800/77, 193 00 Praha 9 - Horní Počernice-Praha 20
        return new Address(null, null,
                stateService.getStateByShortcut("CZ").orElse(null),
                "Ve žlíbku 1800/77", "Praha 9","193 00");
    }

    public void findLatLng() throws ApiException, InterruptedException, IOException {
        GoogleMapsApi api = new GoogleMapsApi();
        LatLng newLL = api.geocode(getFormatAddress());
        if(newLL != null) {
            if (latLng == null)
                latLng = new LatLng();
            latLng.setLat(newLL.getLat());
            latLng.setLng(newLL.getLng());
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}
