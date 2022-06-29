'use strict'

$(function() {

	//以下、クーポン使用時の挙動
	var outputUseCoupon = $('.use-coupon');//実際のご利用ポイント
	var usersCoupon = $('input[name="usersCouponId"]:checked').val();


	$(document).ready(function() {
		outputUseCoupon.text('0円分');//ご利用ポイント
		
	});


	$('.radio').on('change', function() {
		let discountPrice = $(this).parent().children('[name=discountPrice]').val();
		console.log(discountPrice);
		outputUseCoupon.text(discountPrice + '円分');//ご利用クーポン


	});




});