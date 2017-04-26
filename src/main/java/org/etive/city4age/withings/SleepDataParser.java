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
import java.util.List;
import org.apache.log4j.Logger;
import org.etive.city4age.withings.model.City4AgeDateUtils;
import org.etive.city4age.withings.model.Sleep;
import org.etive.city4age.withings.model.SleepPoint;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Parse the data received as a response to a sleep data request to the Withings API.
 * @author Craig
 */
public class SleepDataParser {

    public static List<Sleep> parseSleepResponseData(final String content) {
        List<Sleep> sleeps = new ArrayList<>();
        List<SleepPoint> sleepPoints = new ArrayList<>();
        JSONParser parser = new JSONParser();
        try {
            JSONObject obj = (JSONObject) ((JSONObject) parser.parse(content)).get("body");
            JSONArray sleepItems = (JSONArray) obj.get("series");
            populateSleepPoints(sleepItems, sleepPoints);
            populateSleepFromPoints(sleeps, sleepPoints);
        } catch (ParseException e) {
            sleeps = null;
        }
        return sleeps;
    }

    private static void populateSleepFromPoints(List<Sleep> sleeps, List<SleepPoint> sleepPoints) {
        int processed = 0;
        Sleep sleep = new Sleep();
        SleepPoint previous = null;
        for (SleepPoint point : sleepPoints) {
            if (sleep.getPointCount() == 0) {
                // found start
                sleep.setDate(point);
            } else if (point.indicatesNewSleepPeriod(previous) || processed == sleepPoints.size() - 1) {
                // found cycle end
                sleep.setEndDate(previous);
                sleep.setSleepTotalFromSleepPoints();
                sleeps.add(sleep);
                sleep = new Sleep();
                sleep.setDate(point);
            }
            sleep.addSleepPoint(point);
            previous = point;
            processed++;
        }
    }

    /**
     * Adds {@code SleepPoint} instances to a {@code List<SleepPoint>} when the point's field
     * values suggest the start or end of a night's sleep.
     * @param sleepItems  The returned sleep data array from Withings.
     * @param sleepPoints The {@code List} into which new instances are to be added.
     */
    private static void populateSleepPoints(JSONArray sleepItems, List<SleepPoint> sleepPoints) {
        JSONObject sleepItem;
        String endDate;
        String startDate;
        String state;
        for (Object item : sleepItems) {
            sleepItem = (JSONObject) item;
            startDate = sleepItem.get("startdate").toString();
            endDate = sleepItem.get("enddate").toString();
            state = getSleepState(Integer.parseInt(sleepItem.get("state").toString()));
            sleepPoints.add(getSleepPoint(state, startDate, endDate));
        }
    }

    private static SleepPoint getSleepPoint(String aState, String startDate, String endDate) {
        SleepPoint point = new SleepPoint(aState,
                City4AgeDateUtils.convertEpochStringToCalendar(startDate),
                City4AgeDateUtils.convertEpochStringToCalendar(endDate));
        point.setEndDateTime(null);
        point.setStartDateTime(null);
        point.setFormattedDuration(null);
        return point;
    }

    private static String getSleepState(final int stateCode) {
        String description;
        switch (stateCode) {
            case 0:
                description = "awake";
                break;
            case 1:
                description = "light sleep";
                break;
            case 2:
                description = "deep sleep";
                break;
            case 3:
                description = "REM sleep";
                break;
            default:
                description = "unknown";
        }
        return description;
    }
}
