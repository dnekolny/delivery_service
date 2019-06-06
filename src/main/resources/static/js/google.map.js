var directionsService;
var directionsDisplay;

function initMap() {
    directionsService = new google.maps.DirectionsService;
    directionsDisplay = new google.maps.DirectionsRenderer;
    //geocoder = new google.maps.Geocoder();

    var map = new google.maps.Map(document.getElementById('map'), {
        zoom: 8,
        center: {lat: 49.743292, lng: 15.526285} //50.079170, 14.430859
    });
    directionsDisplay.setMap(map);

    /*var onChangeHandler = function() {
        calculateAndDisplayRoute(directionsService, directionsDisplay);
    };
    document.getElementById('start').addEventListener('change', onChangeHandler);
    document.getElementById('end').addEventListener('change', onChangeHandler);*/
}


function calculateAndDisplayRoute(start, end) {
    directionsService.route({
        origin: start,
        destination: end,
        travelMode: 'DRIVING'
    }, function(response, status) {
        if (status === 'OK') {
            window.directionsDisplay.setDirections(response);
        } else {
            window.alert('Directions request failed due to ' + status);
        }
    });
}

/*marker.addListener('click', function() {
                        $('#modalOrder').modal('show');
                        map.setCenter(marker.getPosition());
                    });*/

//var content = address; //TODO vytvořit HTML s tlačítkem na přidání do seznamu

/*var content = '<div id="content">'+
    '<h6 class="h6 mb-3">'+ address +'</h6>'+
    '<div class="clearfix position-relative">'+

    '<div class="float-left">'+
    '<p class="m-2">\n'+
    '   <span>' + stateName + ': </span>\n' +
    '   <span class="font-weight-bold">' + state + '</span>\n' +
    '</p>'+
    '<p class="m-2">\n'+
    '   <span>' + pickupTypename + ': </span>\n' +
    '   <span class="font-weight-bold">' + pickupType + '</span>\n' +
    '</p>'+
    '<p class="m-2">\n'+
    '   <span>' + sizeCategoryName + ': </span>\n' +
    '   <span class="font-weight-bold">' + sizeCategory + '</span>\n' +
    '</p>'+
    '</div>'+

    '<button type="button" id="btnTruck_' + orderId + '" \n'+
    'class="btn btn-sm btn-outline-success btnTruck position-absolute" style="bottom: 5px; right: 5px;" \n' +
    'style="cursor: pointer;" data-order-id="' + orderId + '"' +
    'onclick="addOrderToDeliverList(\''+orderId+'\');"> ' + btnTitle + ' </button>' +

    '</div>'+
    '</div>';*/

/*google.maps.event.addListener(marker,'click', (function(marker,content,infowindow){
    return function() {
        modal.modal("show");
        //infowindow.setContent(content);
        //infowindow.open(map,marker);
    };
})(marker,content,infowindow));*/
