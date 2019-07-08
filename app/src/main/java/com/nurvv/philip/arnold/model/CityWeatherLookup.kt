package com.nurvv.philip.arnold.model

class CityWeatherLookup() {
    var cityLookup: CityLookup? = null
    var weather: Weather? = null

    constructor(cityLookup: CityLookup): this() {
        this.cityLookup = cityLookup
    }
    constructor(cityLookup: CityLookup, weather: Weather): this() {
        this.cityLookup = cityLookup
        this.weather = weather
    }
}