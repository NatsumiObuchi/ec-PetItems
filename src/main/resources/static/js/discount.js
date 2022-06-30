'use strict'

$(function() {

	// ----------ポイントJS------------

	//「一部のポイントを利用する」のテキストをクリックすると
	$('#use-part-point-text').on('click', function() {
		//「一部のポイントを利用する」のラジオボタンにチェックが入る
		$('#use-part-point-radio').prop('checked', true);
	});

	
	//以下、ポイント使用時の挙動
	var outputUsePoint = $('.use-point');//実際のご利用ポイント
	var sessionPoint = $('#total-point');//session内のポイント総額
	var totalPoint = sessionPoint.data('total-point');
	var sessionPrice = $('#total-price-answer');//session内の合計金額
	var totalPrice = sessionPrice.data('total-price');
	// 各メソッドで上書きして使用する変数
	var partPoint = 0;
	var notUsePoint = 0;

	//ご利用ポイント、獲得予定ポイントの表示
	$(document).ready(function() {
		$('#use-point').show();//「ご利用ポイント」の文字
		$('.use-point').text('0pt');//ご利用ポイント
		$('.total-point-text').show();
		var acquirePoint = totalPrice * 0.01;
		var value = parseInt(acquirePoint, 10);
		$('.prospect-acquire-point').text(value + 'pt');
	});

	//「ポイントを使用しない」を押した時の挙動
	$('#not-all-use-point').on('click', function() {
		console.log('111111');
		var nonPoint = $('#not-all-use-point').val();
		outputUsePoint.text(nonPoint.toLocaleString() + 'pt');//ご利用ポイント(0ポイント)
		partPoint = 0;
		notUsePoint = 0;
		var totalPriceResult = totalPrice - discountCouponPrice;
		$('#total-price-answer').text(totalPriceResult.toLocaleString() + '円');//合計金額
		$('#total-point').text(totalPoint + 'pt');//ポイント残高
		$('#use-part-point-text').val('');//「一部のポイントを利用する」のテキストは空欄にする
	});

	//「全ポイントを使用する」を押した時の挙動
	$('#all-use-point').on('click', function() {
		console.log('222222');
		if (totalPrice <= totalPoint) {//合計金額よりもポイント残高の方が大きかった場合
			partPoint = 0;
			notUsePoint = 1;
			var totalPointResult = totalPoint - totalPrice;
			outputUsePoint.text(totalPrice.toLocaleString() + 'pt');//ご利用ポイント=今回の合計金額分
			$('#total-price-answer').text('0円');//合計金額
			$('#total-point').text(totalPointResult + 'pt');//ポイント残高

		} else {
			partPoint = 0;
			notUsePoint = 1;
			var totalPriceResult = totalPrice - totalPoint - discountCouponPrice;
			outputUsePoint.text(totalPoint.toLocaleString() + 'pt');//ご利用ポイント(全額ポイント)
			$('#total-price-answer').text(totalPriceResult.toLocaleString() + '円');//合計金額
			$('#total-point').text('0pt');//ポイント残高は0ptになる

		}
		$('#use-part-point-text').val('');//「一部のポイントを利用する」のテキストは空欄にする
	});

	//「一部のポイントを利用する」のラジオボタンを押したとき
	$('#use-part-point-radio').on('click', function() {
		console.log('3333333');
		outputUsePoint.text('0pt');//ご利用ポイント(0ポイント)
		var totalPriceResult = totalPrice - discountCouponPrice;
		$('#total-price-answer').text(totalPriceResult.toLocaleString() + '円');//合計金額
		$('#total-point').text(totalPoint.toLocaleString() + 'pt');//ポイント残高
	});

	//「一部のポイントを利用する」でポイントを入力する際の挙動
	$('#use-part-point-text').on('keyup', function() {//inputでもいけた
		console.log('4444444');
		var input = $(this);//テキストに入力したポイント数
		var value = input.val();//テキストに入力した値を変数に代入
		partPoint = value;
		var answer = totalPoint - value;//ポイント数合計
		notUsePoint = 1;
		if (totalPoint < value) {
			$('#error-point-message').show();
			alert('ポイント残高を超えての決済は出来ません。\n' + '修正してください。');
			outputUsePoint.text('0pt');//ご利用ポイント
			$('#total-price-answer').text(totalPrice.toLocaleString() + '円');//合計金額
			$('#total-point').text(totalPoint.toLocaleString() + 'pt');//ポイント残高
			$('#use-part-point-text').val('');//「一部のポイントを利用する」のテキストは空欄にする
		} else {
			var totalPriceResult = totalPrice - value - discountCouponPrice;//合計金額の計算
			$('#use-point').show();
			outputUsePoint.text(value.toLocaleString() + 'pt');//ご利用ポイント
			$('#total-price-answer').text(totalPriceResult.toLocaleString() + '円');//合計金額
			$('#total-point').text(answer.toLocaleString() + 'pt');//ポイント残高
		}
	});

	// ----------クーポンJS------------

	//以下、クーポン使用時の挙動
	var outputUseCoupon = $('.use-coupon');//ご利用クーポン（円）
	var discountCouponPrice = 0;

	$(document).ready(function() {
		outputUseCoupon.text('0円分');//ご利用クーポン（初期表示は０円）
	});

	// クーポンを選択したときの挙動
	$('.radio').on('change', function() {
		var discountPrice = $(this).parent().children('[name=discountPrice]').val();
		console.log(discountPrice);
		discountCouponPrice = discountPrice;
		outputUseCoupon.text(discountPrice + '円分');//ご利用クーポン（円）
		//一部ポイントを利用する場合
		if (partPoint != 0) {
			var totalPriceResult = totalPrice - discountPrice - partPoint;
			//全ポイントを利用する場合
		} else if (partPoint == 0 && notUsePoint != 0) {
			var totalPriceResult = totalPrice - discountPrice - totalPoint;
			//ポイントを利用せずクーポンのみ使う場合
		} else if (partPoint == 0 && notUsePoint == 0) {
			var totalPriceResult = totalPrice - discountPrice;
		}
		$('#total-price-answer').text(totalPriceResult.toLocaleString() + '円');//合計金額
	});

	//「クーポンを利用しない」を選択したときの挙動
	$('#not-use-coupon').on('click', function() {
		discountCouponPrice = 0;
		outputUseCoupon.text('0円分');//ご利用クーポン（初期表示は０円）
		//一部ポイントを利用する場合
		if (partPoint != 0) {
			var totalPriceResult = totalPrice - partPoint;
			console.log('AAAAA');
			//全ポイントを利用する場合
		} else if (partPoint == 0 && notUsePoint != 0) {
			var totalPriceResult = totalPrice - totalPoint;
			console.log('BBBBB');
		} else {
			var totalPriceResult = totalPrice;
			console.log('CCCCC');
		}
		$('#total-price-answer').text(totalPriceResult.toLocaleString() + '円');//合計金額

	});
});