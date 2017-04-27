package org.etive.city4age.repository

class UrlMappings {

    static mappings = {
        group("/api") {
            get "/activity" (controller: 'activity', action: 'index')
            get "/beacon" (controller: 'beacon', action: 'index')
            get "/device" (controller: 'device', action: 'index')
            post "/device" (controller: 'device', action: 'save')
            put "/device/$uuid" (controller: 'device', action: 'update')
            get "/event" (controller: 'event', action: 'index')
            post "/event" (controller: 'event', action: 'save')
            get "/location" (controller: 'location', action: 'index')
            get "/receiver" (controller: 'receiver', action: 'index')
            post "/receiver" (controller: 'receiver', action: 'save')
            get "/receiver/$email" (controller: 'receiver', action: 'show')
            get "/sleep" (controller: 'sleep', action: 'index')
        }

        "500"(view: '/error')
        "404"(view: '/notFound')
    }
}