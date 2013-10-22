/**
 * This can be used if we want to access the Hipstacaster backend directly from the receiver app.
 */
var client = (function($) {

    var _baseRestPath = '/hipstacaster/rest';

    function _search(tags, page) {
        return $.ajax({
            type: 'GET',
            url: _baseRestPath +'/photos/search/' + tags + '?page=' + page + '&perPage=20',
            dataType: 'json',
            async: false
        });
    }


    return {
        search : function(tags, page){
            return JSON.parse(_search(tags, page).responseText);
        }
    };
})(jQuery);