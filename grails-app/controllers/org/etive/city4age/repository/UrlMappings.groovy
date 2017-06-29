package org.etive.city4age.repository

import grails.util.Environment

class UrlMappings {

    static mappings = {
            get "/activity" (controller: 'activityRecord', action: 'index')
            get "/beacon" (controller: 'beacon', action: 'index')
            post "/beacon" (controller: 'beacon', action: 'save')
            get "/device" (controller: 'device', action: 'index')
            post "/device" (controller: 'device', action: 'save')
            put "/device/$uuid" (controller: 'device', action: 'update')
            get "/proximity" (controller: 'proximityEvent', action: 'index')
            post "/proximity" (controller: 'proximityEvent', action: 'save')
            get "/location" (controller: 'location', action: 'index')
            post "/location" (controller: 'location', action: 'save')
            get "/receiver" (controller: 'careReceiver', action: 'index')
            post "/receiver" (controller: 'careReceiver', action: 'save')
            get "/receiver/$email" (controller: 'careReceiver', action: 'show')
            get "/receiver/$receiverId/activity" (controller: 'activityRecord', action: 'index')
            get "/receiver/$receiverId/poi" (controller: 'poiEvent', action: 'index')
            get "/receiver/$receiverId/proximity" (controller: 'proximityEvent', action: 'index')
            get "/receiver/$receiverId/sleep" (controller: 'sleepRecord', action: 'index')
//            get "/receiver/$receiverId/track" (controller: 'track', action: 'index')
//            post "/receiver/$receiverId/track" (controller: 'track', action: 'save')
            get "/region" (controller: 'region', action: 'index')
            get "/sleep" (controller: 'sleepRecord', action: 'index')
//            get "/track" (controller: 'track', action: 'index')
//            post "/track" (controller: 'track', action: 'save')

            "500"(view: '/error')
            "404"(view: '/notFound')
    }
}