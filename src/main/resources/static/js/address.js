'use strict'

$(function(){
	$(document).on('click','#addressBtn',function(){
		$.ajax({
			url: 'http://zipcoda.net/api',
			dataType: 'jsonp',
			data: {
				zipcode: $('#zipcode').val()
			},
			async: true
		}).done(function(data){
			console.dir(JSON.stringify(data));
			$('#address').val(data.items[0].pref + data.items[0].address);
		}).fail(function(XMLHttpRequest, textStatus, errorThrown){
			alert('正しい結果を得られませんでした。');
			console.log('XMLHttpRequest:' + XMLHttpRequest.status);
			console.log('textStatus   :' + textStatus);
			console.log('errorThrown   :' + errorThrown);
		});
	});
});
