package com.adnanali.foodish.Interface;


import com.adnanali.foodish.Model.CustomDictionary;

public interface ConnectorInterface {
     void changeFragment(boolean isAddToBackStack, android.support.v4.app.Fragment fragment, CustomDictionary... dictionary);
     void removeFragment(android.support.v4.app.Fragment fragment);
     void changeTitle(String title);
}