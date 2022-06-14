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
	/*クレジットカードの挙動*/
	 $("#creCard").on('click',function(){
	 	$("#cardNumber").show();
	 });
	 $("#payMoney").on("click",function(){
	 	$("#cardNumber").hide();
	 });

	var grayThemeCreditly = Creditly.initialize(
		'.creditly-wrapper.gray-theme .expiration-month-and-yaer',
		'.creditly-wrapper.gray-theme .credit-card-number',
		'.creditly-wrapper.gray-theme .security-code',
		'.creditly-wrapper.gray-theme .card-type');

	$(".creditly-gray-theme-submit").click(function(e) {
		e.preventDefault();
		var output = grayThemeCreditly.validate();
		if (output) {
			console.log(output);
		}
	});
});



