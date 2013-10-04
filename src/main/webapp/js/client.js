
var client = (function($) {

    var _baseRestPath = '/hipstagram/rest';


    function _getPopularAlbums() {
        return $.ajax({
            type: 'GET',
            url: _baseRestPath +'/albums/popular',
            dataType: 'json',
            async: false,
            cache: false
        });
    }

    function _getPhoto(photoId) {
        return $.ajax({
            type: 'GET',
            url: _baseRestPath +'/photos/' + photoId,
            dataType: 'json',
            async: false,
            cache: false
        });
    }


    return {
        getPopularAlbums : function(){
            return JSON.parse(_getPopularAlbums().responseText);
        },

        getPhoto : function(photoId) {
            return JSON.parse(_getPhoto(photoId).responseText);
        }
    };
})(jQuery);