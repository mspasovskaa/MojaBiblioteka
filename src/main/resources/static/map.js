
const successCallback=(position) => {
    const latitude1 = position.coords.latitude;
    const longitude1 = position.coords.longitude;
    require([
        "esri/config",
        "esri/Map",
        "esri/views/MapView",

        "esri/Graphic",
        "esri/rest/route",
        "esri/rest/support/RouteParameters",
        "esri/rest/support/FeatureSet"

    ], function(esriConfig, Map, MapView, Graphic, route, RouteParameters, FeatureSet) {

        esriConfig.apiKey = "AAPK664e788b931440faa25103947eb7ae10keFmssVXc-rfwusYXhb94TBgeJLSoC95vkYya1QHV22Yh0nNhvvUldCE1SRQs7Xx";

        const map = new Map({
            basemap: "arcgis-navigation"
        });



        const view = new MapView({
            container: "viewDiv",
            map: map,
            center: [longitude1,latitude1],
            zoom: 13
        });

        const routeUrl = "https://route-api.arcgis.com/arcgis/rest/services/World/Route/NAServer/Route_World";


        view.on("click",function(event){
            const locationLon = document.getElementById("longitude").getAttribute("value");
            const locationLat = document.getElementById("latitude").getAttribute("value");
            console.log(locationLon,locationLat);

            const point1 = { //Create a point
                type: "point",
                longitude: longitude1,
                latitude: latitude1
            };


            addGraphic("origin", point1);

            const point2 = {
                type: "point",
                longitude:locationLon,
                latitude: locationLat
            };
            addGraphic("destination", point2);

            getRoute();


        });

        function addGraphic(type, point) {
            const graphic = new Graphic({
                symbol: {
                    type: "simple-marker",
                    color: (type === "origin") ? "white" : "black",
                    size: "8px"
                },
                geometry: point
            });
            view.graphics.add(graphic);
        }

        function getRoute() {
            const routeParams = new RouteParameters({
                stops: new FeatureSet({
                    features: view.graphics.toArray()
                }),

                returnDirections: true

            });

            route.solve(routeUrl, routeParams)
                .then(function(data) {
                    data.routeResults.forEach(function(result) {
                        result.route.symbol = {
                            type: "simple-line",
                            color: [5, 150, 255],
                            width: 3
                        };
                        view.graphics.add(result.route);
                    });

                    if (data.routeResults.length > 0) {
                        const directions = document.createElement("ol");
                        directions.classList = "esri-widget esri-widget--panel esri-directions__scroller";
                        directions.style.marginTop = "0";
                        directions.style.padding = "15px 15px 15px 30px";
                        const features = data.routeResults[0].directions.features;

                        // Show each direction
                        features.forEach(function(result,i){
                            const direction = document.createElement("li");
                            direction.innerHTML = result.attributes.text + " (" + result.attributes.length.toFixed(2) + " miles)";
                            directions.appendChild(direction);
                        });

                        view.ui.empty("top-right");
                        view.ui.add(directions, "top-right");


                    }

                })

                .catch(function(error){
                    console.log(error);
                })

        }

    });
    var R = 6371;
    var locationLon = parseFloat(document.getElementById("longitude").getAttribute("value").toString());
    var locationLat = parseFloat(document.getElementById("latitude").getAttribute("value").toString());

    var dLat = deg2rad(latitude1-locationLat);
    console.log(dLat)
    var dLon = deg2rad(longitude1-locationLon);
    var a =
        Math.sin(dLat/2) * Math.sin(dLat/2) +
        Math.cos(deg2rad(locationLat)) * Math.cos(deg2rad(latitude1)) *
        Math.sin(dLon/2) * Math.sin(dLon/2)
    ;
    var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    var d = (R * c).toFixed(2);
    function deg2rad(deg) {
        return deg * (Math.PI/180)
    }
    document.getElementById("distanca").innerText="Оддалечени сте од бараната локација: "+d+"km";

};

const errorCallback=(error) =>{
    console.error(error);
};
navigator.geolocation.getCurrentPosition(successCallback,errorCallback);
