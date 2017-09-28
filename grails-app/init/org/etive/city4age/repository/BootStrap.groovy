package org.etive.city4age.repository

import grails.util.Environment

class BootStrap {
    def proximityEventService
    def deviceService
    def careReceiverService

    def init = { servletContext ->
        if (Environment.current != Environment.PRODUCTION) {
            def manorVeseyPractice = new Location(locationId: "ManorVeseyPractice", name: "Manor/Vesey Practice", type: "HealthPlace:GP", address: "61 Holland Rd, Sutton Coldfield, B72 1RL", latitude: 52.558301, longitude: -1.823587).save(failOnError: true)
            def manorAshfurlongMedicalCentre = new Location(locationId: "ManorAshfurlongMedicalCentre", name: "Manor/Ashfurlong Medical Centre", type: "HealthPlace:GP", address: "233 Tamworth Rd, Sutton Coldfield, B75 6DX", latitude: 52.578182, longitude: -1.804926).save(failOnError: true)
            def leyHillSurgery = new Location(locationId: "LeyHillSurgery", name: "Ley Hill Surgery", type: "HealthPlace:GP", address: "228 Lichfield Rd, Sutton Coldfield, B74 2UE", latitude: 52.582995, longitude: -1.828277).save(failOnError: true)
            def leyHillLloydsPharmacy = new Location(locationId: "LeyHillLloydsPharmacy", name: "Ley Hill, Lloyds Pharmacy", type: "HealthPlace:Pharmacy", address: "228 Lichfield Rd, Sutton Coldfield, B74 2UE", latitude: 52.582995, longitude: -1.828277).save(failOnError: true)
            def fourOaksMedicalCentre = new Location(locationId: "FourOaksMedicalCentre", name: "Four Oaks Medical Centre", type: "HealthPlace:GP", address: "Carlton House, 18 Mere Green Rd, Sutton Coldfield, B75 5BS", latitude: 52.587231, longitude: -1.827727).save(failOnError: true)
            def sainsburysMereGreen = new Location(locationId: "SainsburysMereGreen", name: "Sainsburys, Mere Green", type: "Shop:Supermarket", address: "30 Mere Green Rd, Sutton Coldfield, B75 5BT", latitude: 52.586966, longitude: -1.825429).save(failOnError: true)
            def stGilesHospice = new Location(locationId: "StGilesHospice", name: "St Giles Hospice", type: "Shop", address: "15 Mere Green Rd, Sutton Coldfield, B75 5BL", latitude: 52.587679, longitude: -1.828829).save(failOnError: true)
            def stGilesHospiceBookShop = new Location(locationId: "StGilesHospiceBookShop", name: "St Giles Hospice Book Shop", type: "Shop", address: "284A Lichfield Rd, Sutton Coldfield, B74 2UG", latitude: 52.585722, longitude: -1.829197).save(failOnError: true)
            def lloydsPharmacy = new Location(locationId: "LloydsPharmacy", name: "Lloyds Pharmacy", type: "Shop:Pharmacy", address: "Unit 1, 290 Lichfield Rd, Sutton Coldfield, B74 2UG", latitude: 52.586177, longitude: -1.829405).save(failOnError: true)
            def msMereGreenFoodhall = new Location(locationId: "MsMereGreenFoodhall", name: "M&S Mere Green Foodhall", type: "Shop:Supermarket", address: "304 Lichfield Rd, Sutton Coldfield, B74 2UG", latitude: 52.586463, longitude: -1.829842).save(failOnError: true)
            def bistrotPierre = new Location(locationId: "BistrotPierre", name: "Bistrot Pierre", type: "SocializingPlace:Restaurant", address: "2 Mere Green Rd, Sutton Coldfield, B75 5BP", latitude: 52.587403, longitude: -1.829227).save(failOnError: true)
            def theOldSchoolHouse = new Location(locationId: "TheOldSchoolHouse", name: "The Old School House", type: "SocializingPlace:Restaurant", address: "12 Mere Green Rd, Sutton Coldfield, B75 5BL", latitude: 52.587532, longitude: -1.828316).save(failOnError: true)
            def mereGreenCommunityCentre = new Location(locationId: "MereGreenCommunityCentre", name: "Mere Green Community Centre", type: "SocializingPlace:SeniorCenter", address: "30A Mere Green Rd, Sutton Coldfield, B75 5BT", latitude: 52.586428, longitude: -1.8274).save(failOnError: true)
            def mereGreenLibrary = new Location(containedBy: mereGreenCommunityCentre, locationId: "MereGreenLibrary", name: "Mere Green Library", type: "SocializingPlace:OtherSocialPlace", address: "30A Mere Green Rd, Sutton Coldfield, B75 5BT", latitude: 52.586635, longitude: -1.827299).save(failOnError: true)
            def theMarePoolWetherspoons = new Location(locationId: "TheMarePoolWetherspoons", name: "The Mare Pool (Wetherspoons)", type: "SocializingPlace:Restaurant", address: "297 Lichfield Rd, Sutton Coldfield, B74 2UG", latitude: 52.586504, longitude: -1.828528).save(failOnError: true)
            def waitroseTheHighgateCentre = new Location(locationId: "WaitroseTheHighgateCentre", name: "Waitrose, The Highgate Centre", type: "Shop:Supermarket", address: "The Highgate Centre, 7 Belwell Lane, Sutton Coldfield, B74 4AB", latitude: 52.587669, longitude: -1.831764).save(failOnError: true)
            def postOfficeInTescoExpress = new Location(locationId: "PostOfficeInTescoExpress", name: "Post Office (in Tesco Express)", type: "Shop", address: "80 Walsall Rd, Sutton Coldfield, B74 4QY", latitude: 52.590205, longitude: -1.848042).save(failOnError: true)
            def costaCoffeePrincessAliceRetailPark = new Location(locationId: "CostaCoffeePrincessAliceRetailPark", name: "Costa Coffee, Princess Alice Retail Park", type: "SocializingPlace:Restaurant", address: "Princess Alice Drive, Sutton Coldfield, B73 6RB", latitude: 52.551163, longitude: -1.859431).save(failOnError: true)
            def gracechurchShoppingCentre = new Location(locationId: "GracechurchShoppingCentre", name: "Gracechurch Shopping Centre", type: "Shop", address: "210A Parade, Sutton Coldfield, B72 1PA", latitude: 52.562663, longitude: -1.824982).save(failOnError: true)
            def cafeNeroGracechurchShoppingCentre = new Location(containedBy: gracechurchShoppingCentre, locationId: "CafeNeroGracechurchShoppingCentre", name: "Cafe Nero, Gracechurch Shopping Centre", type: "SocializingPlace:Restaurant", address: "56 The Parade, Gracechurch Shopping Centre, Sutton Coldfield, B72 1PD", latitude: 52.562115, longitude: -1.824471).save(failOnError: true)
            def costaCoffeeGracechurchShoppingCentre = new Location(containedBy: gracechurchShoppingCentre, locationId: "CostaCoffeeGracechurchShoppingCentre", name: "Costa Coffee, Gracechurch Shopping Centre", type: "SocializingPlace:Restaurant", address: "182 The Parade, Gracechurch Shopping Centre,  Sutton Coldfield, B72 1PH", latitude: 52.563056, longitude: -1.824492).save(failOnError: true)
            def suttonParkSurgery = new Location(locationId: "SuttonParkSurgery", name: "Sutton Park Surgery", type: "HealthPlace:GP", address: "34 Chester Road North, Sutton Coldfield, B73 6SP", latitude: 52.564477, longitude: -1.878559).save(failOnError: true)
            def bootsMereGreen = new Location(locationId: "BootsMereGreen", name: "Boots, Mere Green", type: "Shop:Pharmacy", address: "16 Mere Green Rd, Sutton Coldfield, B75 5BP", latitude: 52.587386, longitude: -1.828246).save(failOnError: true)
            def cancerResearchMereGreen = new Location(locationId: "CancerResearchMereGreen", name: "Cancer Research, Mere Green", type: "Shop", address: "280 Lichfield Rd, Sutton Coldfield, B74 2UG", latitude: 52.585642, longitude: -1.82922).save(failOnError: true)
            def costaCoffeeWyndleyLeisureCentre = new Location(locationId: "CostaCoffeeWyndleyLeisureCentre", name: "Costa Coffee, Wyndley Leisure Centre", type: "SocializingPlace:Restaurant", address: "Wyndley Leisure Centre, Clifton Rd, Sutton Coldfield, B73 6EB", latitude: 52.562596, longitude: -1.834169).save(failOnError: true)
            def holyCrossStFrancisRcChurch = new Location(locationId: "HolyCrossStFrancisRcChurch", name: "Holy Cross & St Francis RC Church", type: "SocializingPlace:OtherSocialPlace", address: "1 Signal Hayes Rd, Sutton Coldfield, B76 2RS", latitude: 52.550383, longitude: -1.79636).save(failOnError: true)

            new Beacon(beaconId: "c4a00000272b", description: "Above main entrance from car park", location: manorVeseyPractice).save(failOnError: true)
            new Beacon(beaconId: "c4a00000274c", description: "Above entrance from street", location: manorVeseyPractice).save(failOnError: true)
            new Beacon(beaconId: "c4a00000275b", description: "Above public entrance", location: manorAshfurlongMedicalCentre).save(failOnError: true)
            new Beacon(beaconId: "c4a000002760", description: "Left of reception desk above head height", location: leyHillSurgery).save(failOnError: true)
            new Beacon(beaconId: "c4a000002729", description: "Above entrance on right hand side", location: leyHillLloydsPharmacy).save(failOnError: true)
            new Beacon(beaconId: "c4a000002737", description: "On wall through main internal entrance on 1st floor", location: fourOaksMedicalCentre).save(failOnError: true)
            new Beacon(beaconId: "c4a000002759", description: "Above left-hand entrance", location: sainsburysMereGreen).save(failOnError: true)
            new Beacon(beaconId: "c4a000002762", description: "Above right-hand entrance", location: sainsburysMereGreen).save(failOnError: true)
            new Beacon(beaconId: "c4a000002740", description: "Above main entrance", location: stGilesHospice).save(failOnError: true)
            new Beacon(beaconId: "c4a000002773", description: "Above main entrance", location: stGilesHospiceBookShop).save(failOnError: true)
            new Beacon(beaconId: "c4a000002771", description: "Above main entrance", location: lloydsPharmacy).save(failOnError: true)
            new Beacon(beaconId: "c4a000002718", description: "Above side entrance", location: lloydsPharmacy).save(failOnError: true)
            new Beacon(beaconId: "c4a000002750", description: "Above main entrance", location: msMereGreenFoodhall).save(failOnError: true)
            new Beacon(beaconId: "c4a000002738", description: "Above Lichfield Road entrance", location: msMereGreenFoodhall).save(failOnError: true)
            new Beacon(beaconId: "c4a000002732", description: "Above main entrance", location: bistrotPierre).save(failOnError: true)
            new Beacon(beaconId: "c4a00000275e", description: "Above main entrance on left (on beam)", location: theOldSchoolHouse).save(failOnError: true)
            new Beacon(beaconId: "c4a00000275a", description: "Above (new) side entrance", location: theOldSchoolHouse).save(failOnError: true)
            new Beacon(beaconId: "c4a00000274f", description: "Above main entrance", location: mereGreenCommunityCentre).save(failOnError: true)
            new Beacon(beaconId: "c4a000002726", description: "1st floor, internal entrance, inside main doors", location: mereGreenLibrary).save(failOnError: true)
            new Beacon(beaconId: "c4a00000272d", description: "Above main entrance", location: theMarePoolWetherspoons).save(failOnError: true)
            new Beacon(beaconId: "c4a000002711", description: "Above side entrance", location: theMarePoolWetherspoons).save(failOnError: true)
            new Beacon(beaconId: "c4a000002751", description: "Above entrance from car park", location: waitroseTheHighgateCentre).save(failOnError: true)
            new Beacon(beaconId: "c4a000002712", description: "Above entrance from street", location: waitroseTheHighgateCentre).save(failOnError: true)
            new Beacon(beaconId: "c4a000002733", description: "Above internal service counter", location: postOfficeInTescoExpress).save(failOnError: true)
            new Beacon(beaconId: "c4a00000271c", description: "Above main entrance", location: costaCoffeePrincessAliceRetailPark).save(failOnError: true)
            new Beacon(beaconId: "c4a000002767", description: "North Mall entrance, right-hand column (BHS)", location: gracechurchShoppingCentre).save(failOnError: true)
            new Beacon(beaconId: "c4a000002768", description: "North Mall entrance, left-hand column (H Samuel)", location: gracechurchShoppingCentre).save(failOnError: true)
            new Beacon(beaconId: "c4a000002754", description: "Link Bridge entrance, under signage beam (centre)", location: gracechurchShoppingCentre).save(failOnError: true)
            new Beacon(beaconId: "c4a00000273f", description: "Centre Mall entrance, right-hand column (Vision Express)", location: gracechurchShoppingCentre).save(failOnError: true)
            new Beacon(beaconId: "c4a00000271b", description: "Centre Mall entrance, left-hand column (Starbucks)", location: gracechurchShoppingCentre).save(failOnError: true)
            new Beacon(beaconId: "c4a00000271d", description: "South Mall entrance, left-hand column (Molloy's)", location: gracechurchShoppingCentre).save(failOnError: true)
            new Beacon(beaconId: "c4a000002722", description: "South Mall entrance, right-hand column (Co-Op Travel)", location: gracechurchShoppingCentre).save(failOnError: true)
            new Beacon(beaconId: "c4a000002743", description: "Above main entrance", location: cafeNeroGracechurchShoppingCentre).save(failOnError: true)
            new Beacon(beaconId: "c4a00000271a", description: "Above door on inside", location: costaCoffeeGracechurchShoppingCentre).save(failOnError: true)
            new Beacon(beaconId: "c4a000002724", description: "Above and left of the reception desk", location: suttonParkSurgery).save(failOnError: true)
            new Beacon(beaconId: "c4a000002764", description: "Above main entrance on right (car park entrance)", location: bootsMereGreen).save(failOnError: true)
            new Beacon(beaconId: "c4a000002746", description: "Above street entrance on right", location: bootsMereGreen).save(failOnError: true)
            new Beacon(beaconId: "c4a00000275f", description: "On pillar right of main entrance", location: cancerResearchMereGreen).save(failOnError: true)
            new Beacon(beaconId: "c4a000002727", description: "Above side entrance to leisure centre reception", location: costaCoffeeWyndleyLeisureCentre).save(failOnError: true)
            new Beacon(beaconId: "c4a000002742", description: "Above main entrance on the right", location: holyCrossStFrancisRcChurch).save(failOnError: true)
        
            if (Environment.current == Environment.TEST) {
                def email1 = "eventlist1@test.org"
                def email2 = "eventlist2@test.org"

                def json = [
                        logbookId: 1234567890,
                        withingsId: 2345678901,
                        emailAddress: email1,
                        accessKey: "key:abcdefghijk",
                        accessSecret: "secret:abcdefghijk",
                        city4ageId: "eu:c4a:user:66",
                        forTest: true
                ]
                careReceiverService.createCareReceiver(json)
                def careReceiver1 = CareReceiver.findByEmailAddress(email1)

                def uuid = "3f1a10a9abe23a08"
                json = [
                        os: "Android",
                        osVersion: "7.0",
                        model: "Moto G (4)",
                        uuid: uuid,
                        token: careReceiver1.token,
                        timestamp: new Date().getTime()
                ]
                deviceService.createDevice(careReceiver1, json)
                def device1 = Device.findByUniqueId(uuid)

                json = [
                        eventType: "found",
                        timestamp: new Date().parse("yyyy-MM-dd'T'HH:mm:ssXXX", "2017-06-20T08:01:36Z").getTime(),
                        rssi: -79,
                        txPower: -47,
                        beaconId: "c4a000002762",
                        token: careReceiver1.token,
                        uuid: device1.uniqueId
                ]
                proximityEventService.createProximityEvent(careReceiver1, json)

                json = [
                        eventType: "found",
                        timestamp: new Date().parse("yyyy-MM-dd'T'HH:mm:ssXXX", "2017-06-20T08:01:25Z").getTime(),
                        rssi: -86,
                        txPower: -47,
                        beaconId: "c4a000002759",
                        token: careReceiver1.token,
                        uuid: device1.uniqueId
                ]
                proximityEventService.createProximityEvent(careReceiver1, json)

                json = [
                        eventType: "found",
                        timestamp: new Date().parse("yyyy-MM-dd'T'HH:mm:ssXXX", "2017-06-20T08:04:38Z").getTime(),
                        rssi: -86,
                        txPower: -47,
                        beaconId: "c4a00000274f",
                        token: careReceiver1.token,
                        uuid: device1.uniqueId
                ]
                proximityEventService.createProximityEvent(careReceiver1, json)

                json = [
                        eventType: "lost",
                        timestamp: new Date().parse("yyyy-MM-dd'T'HH:mm:ssXXX", "2017-06-20T08:07:13Z").getTime(),
                        rssi: -89,
                        rssiMax: -67,
                        beaconId: "c4a00000274f",
                        token: careReceiver1.token,
                        uuid: device1.uniqueId
                ]
                proximityEventService.createProximityEvent(careReceiver1, json)

                json = [
                        eventType: "found",
                        timestamp: new Date().parse("yyyy-MM-dd'T'HH:mm:ssXXX", "2017-06-20T09:05:39Z").getTime(),
                        rssi: -80,
                        txPower: -47,
                        beaconId: "c4a000002726",
                        token: careReceiver1.token,
                        uuid: device1.uniqueId
                ]
                proximityEventService.createProximityEvent(careReceiver1, json)

                json = [
                        eventType: "lost",
                        timestamp: new Date().parse("yyyy-MM-dd'T'HH:mm:ssXXX", "2017-06-20T09:06:19Z").getTime(),
                        rssi: -93,
                        rssiMax: -75,
                        beaconId: "c4a000002726",
                        token: careReceiver1.token,
                        uuid: device1.uniqueId
                ]
                proximityEventService.createProximityEvent(careReceiver1, json)

                json = [
                        eventType: "found",
                        timestamp: new Date().parse("yyyy-MM-dd'T'HH:mm:ssXXX", "2017-06-20T09:07:33Z").getTime(),
                        rssi: -90,
                        txPower: -47,
                        beaconId: "c4a000002726",
                        token: careReceiver1.token,
                        uuid: device1.uniqueId
                ]
                proximityEventService.createProximityEvent(careReceiver1, json)

                json = [
                        eventType: "found",
                        timestamp: new Date().parse("yyyy-MM-dd'T'HH:mm:ssXXX", "2017-06-20T09:24:57Z").getTime(),
                        rssi: -91,
                        txPower: -47,
                        beaconId: "c4a00000274f",
                        token: careReceiver1.token,
                        uuid: device1.uniqueId
                ]
                proximityEventService.createProximityEvent(careReceiver1, json)

                json = [
                        eventType: "lost",
                        timestamp: new Date().parse("yyyy-MM-dd'T'HH:mm:ssXXX", "2017-06-20T09:25:02Z").getTime(),
                        rssi: -96,
                        rssiMax: -77,
                        beaconId: "c4a000002726",
                        token: careReceiver1.token,
                        uuid: device1.uniqueId
                ]
                proximityEventService.createProximityEvent(careReceiver1, json)

                json = [
                        eventType: "lost",
                        timestamp: new Date().parse("yyyy-MM-dd'T'HH:mm:ssXXX", "2017-06-20T09:25:18Z").getTime(),
                        rssi: -90,
                        rssiMax: -70,
                        beaconId: "c4a00000274f",
                        token: careReceiver1.token,
                        uuid: device1.uniqueId
                ]
                proximityEventService.createProximityEvent(careReceiver1, json)

                json = [
                        eventType: "found",
                        timestamp: new Date().parse("yyyy-MM-dd'T'HH:mm:ssXXX", "2017-06-20T09:26:16Z").getTime(),
                        rssi: -94,
                        txPower: -47,
                        beaconId: "c4a000002764",
                        token: careReceiver1.token,
                        uuid: device1.uniqueId
                ]
                proximityEventService.createProximityEvent(careReceiver1, json)

                json = [
                        eventType: "found",
                        timestamp: new Date().parse("yyyy-MM-dd'T'HH:mm:ssXXX", "2017-06-20T09:26:37Z").getTime(),
                        rssi: -84,
                        txPower: -47,
                        beaconId: "c4a000002746",
                        token: careReceiver1.token,
                        uuid: device1.uniqueId
                ]
                proximityEventService.createProximityEvent(careReceiver1, json)

                json = [
                        eventType: "lost",
                        timestamp: new Date().parse("yyyy-MM-dd'T'HH:mm:ssXXX", "2017-06-20T09:26:41Z").getTime(),
                        rssi: -91,
                        rssiMax: -70,
                        beaconId: "c4a000002764",
                        token: careReceiver1.token,
                        uuid: device1.uniqueId
                ]
                proximityEventService.createProximityEvent(careReceiver1, json)

                json = [
                        eventType: "lost",
                        timestamp: new Date().parse("yyyy-MM-dd'T'HH:mm:ssXXX", "2017-06-20T09:27:29Z").getTime(),
                        rssi: -93,
                        rssiMax: -68,
                        beaconId: "c4a000002746",
                        token: careReceiver1.token,
                        uuid: device1.uniqueId
                ]
                proximityEventService.createProximityEvent(careReceiver1, json)

                json = [
                        eventType: "found",
                        timestamp: new Date().parse("yyyy-MM-dd'T'HH:mm:ssXXX", "2017-06-20T09:31:22Z").getTime(),
                        rssi: -88,
                        txPower: -47,
                        beaconId: "c4a000002762",
                        token: careReceiver1.token,
                        uuid: device1.uniqueId
                ]
                proximityEventService.createProximityEvent(careReceiver1, json)

                json = [
                        eventType: "found",
                        timestamp: new Date().parse("yyyy-MM-dd'T'HH:mm:ssXXX", "2017-06-20T09:31:24Z").getTime(),
                        rssi: -88,
                        txPower: -47,
                        beaconId: "c4a000002759",
                        token: careReceiver1.token,
                        uuid: device1.uniqueId
                ]
                proximityEventService.createProximityEvent(careReceiver1, json)

                json = [
                        eventType: "lost",
                        timestamp: new Date().parse("yyyy-MM-dd'T'HH:mm:ssXXX", "2017-06-20T09:31:34Z").getTime(),
                        rssi: -87,
                        rssiMax: -84,
                        beaconId: "c4a000002759",
                        token: careReceiver1.token,
                        uuid: device1.uniqueId
                ]
                proximityEventService.createProximityEvent(careReceiver1, json)

                json = [
                        eventType: "lost",
                        timestamp: new Date().parse("yyyy-MM-dd'T'HH:mm:ssXXX", "2017-06-20T09:31:36Z").getTime(),
                        rssi: -92,
                        rssiMax: -88,
                        beaconId: "c4a000002762",
                        token: careReceiver1.token,
                        uuid: device1.uniqueId
                ]
                proximityEventService.createProximityEvent(careReceiver1, json)

                // ************************************************************

                json = [
                        logbookId: 3456789012,
                        withingsId: 4567890123,
                        emailAddress: email2,
                        accessKey: "key:bcdefghijkl",
                        accessSecret: "secret:bcdefghijkl",
                        city4ageId: "eu:c4a:user:67",
                        forTest: true
                ]
                careReceiverService.createCareReceiver(json)
                def careReceiver2 = CareReceiver.findByEmailAddress(email2)

                uuid = "1386994bfbbaf41e"
                json = [
                        os: "Android",
                        osVersion: "4.4.2",
                        model: "Archos 70b Xenon",
                        uuid: uuid,
                        token: careReceiver2.token,
                        timestamp: new Date().getTime()
                ]
                deviceService.createDevice(careReceiver2, json)
                def device2 = Device.findByUniqueId(uuid)

                json = [
                        eventType: "lost",
                        timestamp: new Date().parse("yyyy-MM-dd'T'HH:mm:ssXXX", "2017-06-21T09:00:03Z").getTime(),
                        rssi: -92,
                        rssiMax: -86,
                        beaconId: "c4a00000274f",
                        token: careReceiver2.token,
                        uuid: device2.uniqueId
                ]
                proximityEventService.createProximityEvent(careReceiver2, json)

                json = [
                        eventType: "found",
                        timestamp: new Date().parse("yyyy-MM-dd'T'HH:mm:ssXXX", "2017-06-21T09:01:02Z").getTime(),
                        rssi: -83,
                        txPower: -47,
                        beaconId: "c4a000002759",
                        token: careReceiver2.token,
                        uuid: device2.uniqueId
                ]
                proximityEventService.createProximityEvent(careReceiver2, json)

                json = [
                        eventType: "lost",
                        timestamp: new Date().parse("yyyy-MM-dd'T'HH:mm:ssXXX", "2017-06-21T08:56:36Z").getTime(),
                        rssi: -89,
                        rssiMax: -71,
                        beaconId: "c4a00000274f",
                        token: careReceiver2.token,
                        uuid: device2.uniqueId
                ]
                proximityEventService.createProximityEvent(careReceiver2, json)

                json = [
                        eventType: "found",
                        timestamp: new Date().parse("yyyy-MM-dd'T'HH:mm:ssXXX", "2017-06-21T09:01:06Z").getTime(),
                        rssi: -84,
                        txPower: -47,
                        beaconId: "c4a000002762",
                        token: careReceiver2.token,
                        uuid: device2.uniqueId
                ]
                proximityEventService.createProximityEvent(careReceiver2, json)

                json = [
                        eventType: "found",
                        timestamp: new Date().parse("yyyy-MM-dd'T'HH:mm:ssXXX", "2017-06-21T08:55:45Z").getTime(),
                        rssi: -78,
                        txPower: -47,
                        beaconId: "c4a00000274f",
                        token: careReceiver2.token,
                        uuid: device2.uniqueId
                ]
                proximityEventService.createProximityEvent(careReceiver2, json)

                json = [
                        eventType: "found",
                        timestamp: new Date().parse("yyyy-MM-dd'T'HH:mm:ssXXX", "2017-06-21T08:59:52Z").getTime(),
                        rssi: -90,
                        txPower: -47,
                        beaconId: "c4a00000274f",
                        token: careReceiver2.token,
                        uuid: device2.uniqueId
                ]
                proximityEventService.createProximityEvent(careReceiver2, json)

                json = [
                        eventType: "lost",
                        timestamp: new Date().parse("yyyy-MM-dd'T'HH:mm:ssXXX", "2017-06-21T09:01:30Z").getTime(),
                        rssi: -91,
                        rssiMax: -74,
                        beaconId: "c4a000002759",
                        token: careReceiver2.token,
                        uuid: device2.uniqueId
                ]
                proximityEventService.createProximityEvent(careReceiver2, json)

                json = [
                        eventType: "found",
                        timestamp: new Date().parse("yyyy-MM-dd'T'HH:mm:ssXXX", "2017-06-21T09:01:51Z").getTime(),
                        rssi: -77,
                        txPower: -47,
                        beaconId: "c4a000002759",
                        token: careReceiver2.token,
                        uuid: device2.uniqueId
                ]
                proximityEventService.createProximityEvent(careReceiver2, json)

                json = [
                        eventType: "lost",
                        timestamp: new Date().parse("yyyy-MM-dd'T'HH:mm:ssXXX", "2017-06-21T09:02:42Z").getTime(),
                        rssi: -89,
                        rssiMax: -77,
                        beaconId: "c4a000002759",
                        token: careReceiver2.token,
                        uuid: device2.uniqueId
                ]
                proximityEventService.createProximityEvent(careReceiver2, json)

                json = [
                        eventType: "found",
                        timestamp: new Date().parse("yyyy-MM-dd'T'HH:mm:ssXXX", "2017-06-21T09:02:47Z").getTime(),
                        rssi: -90,
                        txPower: -47,
                        beaconId: "c4a000002759",
                        token: careReceiver2.token,
                        uuid: device2.uniqueId
                ]
                proximityEventService.createProximityEvent(careReceiver2, json)

                json = [
                        eventType: "lost",
                        timestamp: new Date().parse("yyyy-MM-dd'T'HH:mm:ssXXX", "2017-06-21T09:03:01Z").getTime(),
                        rssi: -92,
                        rssiMax: -75,
                        beaconId: "c4a000002762",
                        token: careReceiver2.token,
                        uuid: device2.uniqueId
                ]
                proximityEventService.createProximityEvent(careReceiver2, json)

                json = [
                        eventType: "found",
                        timestamp: new Date().parse("yyyy-MM-dd'T'HH:mm:ssXXX", "2017-06-21T09:03:09Z").getTime(),
                        rssi: -82,
                        txPower: -47,
                        beaconId: "c4a000002762",
                        token: careReceiver2.token,
                        uuid: device2.uniqueId
                ]
                proximityEventService.createProximityEvent(careReceiver2, json)

                json = [
                        eventType: "lost",
                        timestamp: new Date().parse("yyyy-MM-dd'T'HH:mm:ssXXX", "2017-06-21T09:03:39Z").getTime(),
                        rssi: -93,
                        rssiMax: -70,
                        beaconId: "c4a000002759",
                        token: careReceiver2.token,
                        uuid: device2.uniqueId
                ]
                proximityEventService.createProximityEvent(careReceiver2, json)

                json = [
                        eventType: "lost",
                        timestamp: new Date().parse("yyyy-MM-dd'T'HH:mm:ssXXX", "2017-06-21T09:03:37Z").getTime(),
                        rssi: -92,
                        rssiMax: -74,
                        beaconId: "c4a000002762",
                        token: careReceiver2.token,
                        uuid: device2.uniqueId
                ]
                proximityEventService.createProximityEvent(careReceiver2, json)

                json = [
                        eventType: "found",
                        timestamp: new Date().parse("yyyy-MM-dd'T'HH:mm:ssXXX", "2017-06-21T09:20:06Z").getTime(),
                        rssi: -73,
                        txPower: -47,
                        beaconId: "c4a00000274f",
                        token: careReceiver2.token,
                        uuid: device2.uniqueId
                ]
                proximityEventService.createProximityEvent(careReceiver2, json)

                json = [
                        eventType: "lost",
                        timestamp: new Date().parse("yyyy-MM-dd'T'HH:mm:ssXXX", "2017-06-21T09:20:26Z").getTime(),
                        rssi: -92,
                        rssiMax: -73,
                        beaconId: "c4a00000274f",
                        token: careReceiver2.token,
                        uuid: device2.uniqueId
                ]
                proximityEventService.createProximityEvent(careReceiver2, json)

                json = [
                        eventType: "found",
                        timestamp: new Date().parse("yyyy-MM-dd'T'HH:mm:ssXXX", "2017-06-21T12:34:24Z").getTime(),
                        rssi: -89,
                        txPower: -47,
                        beaconId: "c4a00000274f",
                        token: careReceiver2.token,
                        uuid: device2.uniqueId
                ]
                proximityEventService.createProximityEvent(careReceiver2, json)

                json = [
                        eventType: "found",
                        timestamp: new Date().parse("yyyy-MM-dd'T'HH:mm:ssXXX", "2017-06-21T13:13:53Z").getTime(),
                        rssi: -80,
                        txPower: -47,
                        beaconId: "c4a00000274f",
                        token: careReceiver2.token,
                        uuid: device2.uniqueId
                ]
                proximityEventService.createProximityEvent(careReceiver2, json)

                json = [
                        eventType: "lost",
                        timestamp: new Date().parse("yyyy-MM-dd'T'HH:mm:ssXXX", "2017-06-21T12:35:22Z").getTime(),
                        rssi: -85,
                        rssiMax: -81,
                        beaconId: "c4a00000274f",
                        token: careReceiver2.token,
                        uuid: device2.uniqueId
                ]
                proximityEventService.createProximityEvent(careReceiver2, json)

                json = [
                        eventType: "lost",
                        timestamp: new Date().parse("yyyy-MM-dd'T'HH:mm:ssXXX", "2017-06-21T13:16:42Z").getTime(),
                        rssi: -81,
                        rssiMax: -80,
                        beaconId: "c4a00000274f",
                        token: careReceiver2.token,
                        uuid: device2.uniqueId
                ]
                proximityEventService.createProximityEvent(careReceiver2, json)

                json = [
                        eventType: "found",
                        timestamp: new Date().parse("yyyy-MM-dd'T'HH:mm:ssXXX", "2017-06-21T14:39:11Z").getTime(),
                        rssi: -86,
                        txPower: -47,
                        beaconId: "c4a000002726",
                        token: careReceiver2.token,
                        uuid: device2.uniqueId
                ]
                proximityEventService.createProximityEvent(careReceiver2, json)

                json = [
                        eventType: "found",
                        timestamp: new Date().parse("yyyy-MM-dd'T'HH:mm:ssXXX", "2017-06-21T14:39:27Z").getTime(),
                        rssi: -92,
                        txPower: -47,
                        beaconId: "c4a00000274f",
                        token: careReceiver2.token,
                        uuid: device2.uniqueId
                ]
                proximityEventService.createProximityEvent(careReceiver2, json)

                json = [
                        eventType: "lost",
                        timestamp: new Date().parse("yyyy-MM-dd'T'HH:mm:ssXXX", "2017-06-21T14:39:31Z").getTime(),
                        rssi: -92,
                        rssiMax: -80,
                        beaconId: "c4a000002726",
                        token: careReceiver2.token,
                        uuid: device2.uniqueId
                ]
                proximityEventService.createProximityEvent(careReceiver2, json)

                json = [
                        eventType: "lost",
                        timestamp: new Date().parse("yyyy-MM-dd'T'HH:mm:ssXXX", "2017-06-21T14:39:48Z").getTime(),
                        rssi: -91,
                        rssiMax: -76,
                        beaconId: "c4a00000274f",
                        token: careReceiver2.token,
                        uuid: device2.uniqueId
                ]
                proximityEventService.createProximityEvent(careReceiver2, json)
            }
        }
        Region.addLocations(Location.list())
        City4AgeTrustManager.trustAllCertificates()
    }

    def destroy = {
    }
}