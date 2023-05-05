package com.driver.controllers;

import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class HotelManagementRepository {
    HashMap<String,Hotel>hotelMap=new HashMap<>();
    HashMap<Integer, User>userMap=new HashMap<>();
    HashMap<String,Booking>bookingMap=new HashMap<>();
    public String addHotel(Hotel hotel) {
        //incase the hotelName is null or the hotel Object is null return an empty a FAILURE
        if(hotel.getHotelName()==null) return "FAILURE";
            //Incase somebody is trying to add the duplicate hotelName return FAILURE
        else if(hotelMap.containsKey((hotel.getHotelName())))return "FAILURE";
            //in all other cases return SUCCESS after successfully adding the hotel to the hotelDb.
        else{
            hotelMap.put(hotel.getHotelName(),hotel);
        }
        return "SUCCESS";
    }

    public Integer addUser(User user) {
        //You need to add a User Object to the database
        //Assume that user will always be a valid user and return the aadharCardNo of the user
        userMap.put(user.getaadharCardNo(),user);
        return user.getaadharCardNo();
    }

    public String getHotelWithMostFacilities() {
        //Out of all the hotels we have added so far, we need to find the hotelName with most no of facilities
        //Incase there is a tie return the lexicographically smaller hotelName
        //Incase there is not even a single hotel with atleast 1 facility return "" (empty string)
        int max_facility=0;
        String hotelName_maxfacility="";
        for(String hotelName:hotelMap.keySet()){
            Hotel hotel=hotelMap.get(hotelName);
            if(hotel.getFacilities().size()>max_facility){
                hotelName_maxfacility=hotelName;
                max_facility=hotel.getFacilities().size();
            }else if(hotel.getFacilities().size()==max_facility){
                if(hotelName.compareTo(hotelName_maxfacility)<0){
                    hotelName_maxfacility=hotelName;
                }
            }
        }
        return hotelName_maxfacility;
    }

    public int bookRoom(Booking booking) {
        //The booking object coming from postman will have all the attributes except bookingId and amountToBePaid;
        //Have bookingId as a random UUID generated String
        //save the booking Entity and keep the bookingId as a primary key
        //Calculate the total amount paid by the person based on no. of rooms booked and price of the room per night.
        //If there arent enough rooms available in the hotel that we are trying to book return -1
        //in other case return total amount paid
        UUID uuid=UUID.randomUUID();
        String bookingId=uuid.toString();
        booking.setBookingId(bookingId);
        String hotelName=booking.getHotelName();
        Hotel hotel=hotelMap.get(hotelName);
        int availableRooms=hotel.getAvailableRooms();
        int pricePerNight=hotel.getPricePerNight();
        int noOfRooms=booking.getNoOfRooms();
        int Total_amount;
        if(noOfRooms>availableRooms)return -1;
        Total_amount=noOfRooms*pricePerNight;
        booking.setAmountToBePaid(Total_amount);
        hotel.setAvailableRooms(availableRooms-noOfRooms);
        bookingMap.put(bookingId,booking);
        hotelMap.put(hotelName,hotel);
        return Total_amount;
    }

    public int getBooking(Integer aadharCard) {
        //In this function return the bookings done by a person
        int noOfBooking=0;
        for(String bookingId:bookingMap.keySet()){
            Booking booking=bookingMap.get(bookingId);
            if(booking.getBookingAadharCard()==aadharCard)
                noOfBooking++;
        }
        return noOfBooking;
    }

    public Hotel updateFacilities(List<Facility> newFacilities, String hotelName) {
        //We are having a new facilites that a hotel is planning to bring.
        //If the hotel is already having that facility ignore that facility otherwise add that facility in the hotelDb
        //return the final updated List of facilities and also update that in your hotelDb
        //Note that newFacilities can also have duplicate facilities possible
        Hotel hotel=hotelMap.get(hotelName);
        List<Facility>list=hotel.getFacilities();
        for(Facility facility:newFacilities){
            if(list.contains(facility))continue;
            else list.add(facility);
        }
        hotel.setFacilities(list);
        hotelMap.put(hotelName,hotel);
        return hotel;
    }
}
