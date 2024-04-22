package edu.CodeRed.tests;
import edu.CodeRed.entities.user;
import edu.CodeRed.services.userservice;






 class mainClass {
            public static void main(String[] args) {

                user u = new user("mlik", "ons", "2024-04-13 15:30:00", "femme", "Sousse", "sdfghj", "hbjnben@", "ben.ons@esprit.tn");

                userservice us = new userservice();

                us.addUser(u);
//us.DeleteUser(20);
//us.UpdatUser(user);
            }}