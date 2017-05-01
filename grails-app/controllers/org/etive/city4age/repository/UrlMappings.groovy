package org.etive.city4age.repository

class UrlMappings {

    static mappings = {
        group("/api") {
            get "/activity" (controller: 'activityRecord', action: 'index')
            get "/beacon" (controller: 'beacon', action: 'index')
            get "/device" (controller: 'device', action: 'index')
            post "/device" (controller: 'device', action: 'save')
            put "/device/$uuid" (controller: 'device', action: 'update')
            get "/proximity" (controller: 'proximityEvent', action: 'index')
            post "/proximity" (controller: 'proximityEvent', action: 'save')
            get "/location" (controller: 'location', action: 'index')
            get "/receiver" (controller: 'careReceiver', action: 'index')
            post "/receiver" (controller: 'careReceiver', action: 'save')
            get "/receiver/$email" (controller: 'careReceiver', action: 'show')
            get "/sleep" (controller: 'sleepRecord', action: 'index')
        }

        "500"(view: '/error')
        "404"(view: '/notFound')
    }
}