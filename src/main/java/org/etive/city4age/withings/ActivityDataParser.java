/*
 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
 * Copyright: Etive Technologies Ltd. 2016 (www.etive.org). All rights reserved.
 * Created  : 14-Dec-2016
 * Author   : Craig, (c dot speedie at etive dot org)
 * Enquiries: please send any enquiries to hello at etive dot org
 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
 */
package org.etive.city4age.withings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import org.apache.log4j.Logger;
import org.etive.city4age.withings.model.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Parse the data received as a response to an activity data request to the Withings API.
 * @author Craig
 */
public class ActivityDataParser {

    private final Logger logger = Logger.getLogger(this.getClass());

    /**
     * Parse the response data into a {@code Map}.
     * @param content The respnse body text to parse.
     * @return A {@code Map} containing the following:
     * <ul>
     *   <li>key: calories; value: {@code List<WithingsData>}. A list of {@code Calories} instances.</li>
     *   <li>key: distance; value: {@code List<WithingsData>}. A list of {@code Distance} instances.</li>
     *   <li>key: pace; value: {@code List<WithingsData>}. A list of {@code Pace} instances.</li>
     *   <li>key: steps; value: {@code List<WithingsData>}. A list of {@code Steps} instances.</li>
     * </ul>
     */
    public Map<String, List<WithingsData>> parseActivityResponseBody(final String content) {
        Map<String, List<WithingsData>> theData = new HashMap<>();
        JSONParser parser = new JSONParser();
        List<WithingsData> calories = new ArrayList<>();
        List<WithingsData> distance = new ArrayList<>();
        List<WithingsData> pace = new ArrayList<>();
        List<WithingsData> steps = new ArrayList<>();
        try {
            JSONObject obj = (JSONObject) ((JSONObject) parser.parse(content)).get("body");
            JSONArray activities = (JSONArray) obj.get("activities");
            JSONObject activity;
            String date;
            for (Object item : activities) {
                activity = (JSONObject) item;
                date = City4AgeDateUtils.switchDatePartPositions(activity.get("date").toString());
                calories.add(getCalories(date, activity.get("calories").toString(), activity.get("totalcalories").toString()));
                distance.add(getDistance(date, activity.get("distance").toString()));
                pace.add(getPace(date, activity.get("soft").toString(), activity.get("moderate").toString(), activity.get("intense").toString()));
                steps.add(getSteps(date, activity.get("steps").toString()));
            }
            TreeSet<WithingsData> calTree = new TreeSet<>();
            Collections.sort(calories);
            Collections.sort(distance);
            Collections.sort(pace);
            Collections.sort(steps);
            theData.put("calories", calories);
            theData.put("distance", distance);
            theData.put("pace", pace);
            theData.put("steps", steps);
        } catch (ParseException e) {
            logger.error("cannot parse JSON: " + e.getMessage());
        }
        return theData;
    }

    private Calories getCalories(String date, String calories, String totalCalories) {
        Calories cal = new Calories();
        cal.setDate(date);
        cal.setCalories(Double.parseDouble(calories));
        cal.setTotalCalories(Double.parseDouble(totalCalories));
        return cal;
    }

    private Distance getDistance(String date, String distance) {
        Distance dis = new Distance();
        dis.setDate(date);
        dis.setDistance(Float.parseFloat(distance));
        return dis;
    }

    private Pace getPace(String date, String soft, String moderate, String intense) {
        Pace pace = new Pace();
        pace.setDate(date);
        pace.setIntense(Integer.parseInt(intense));
        pace.setModerate(Integer.parseInt(moderate));
        pace.setSoft(Integer.parseInt(soft));
        return pace;
    }

    private Steps getSteps(String date, String distance) {
        Steps steps = new Steps();
        steps.setDate(date);
        steps.setSteps(Integer.parseInt(distance));
        return steps;
    }
}
