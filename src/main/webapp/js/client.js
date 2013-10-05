
var client = (function($) {

    var _baseRestPath = '/hipstacaster/rest';

    function _search(tags) {
        return $.ajax({
            type: 'GET',
            url: _baseRestPath +'/photos/search/' + tags,
            dataType: 'json',
            async: false
        });
    }

    function _getPopularAlbums() {
        return $.ajax({
            type: 'GET',
            url: _baseRestPath +'/albums/popular',
            dataType: 'json',
            async: false
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

    function _getPhotosOfAlbum(albumId) {
        return $.ajax({
            type: 'GET',
            url: _baseRestPath +'/albums/' + albumId + '/photos',
            dataType: 'json',
            async: false,
            cache: false
        });
    }


    return {
        search : function(tags){
            return JSON.parse(_search(tags).responseText);
        },

        getPopularAlbums : function(){
            return JSON.parse(_getPopularAlbums().responseText);
        },

        getPhoto : function(photoId) {
            return JSON.parse(_getPhoto(photoId).responseText);
        },

        getPhotosOfAlbum : function(albumId) {
            return JSON.parse(_getPhotosOfAlbum(albumId).responseText);
        }
    };
})(jQuery);