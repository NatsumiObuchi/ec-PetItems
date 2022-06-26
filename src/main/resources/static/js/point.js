'use strict'

$(function () {
	//「一部のポイントを利用する」のテキストをクリックすると
  	$('#use-part-point-text').on('click', function(){
		//「一部のポイントを利用する」のラジオボタンにチェックが入る
		$('#use-part-point-radio').prop('checked', true);
	});
	
	
	//以下、ポイント使用時の挙動
	$('#use-point').hide();//「ご利用ポイント」の文字
	$('.total-point-text').show();
	var outputUsePoint = $('.use-point');//実際のご利用ポイント
	var sessionPoint = $('#total-point');//session内のポイント総額
	var totalPoint = sessionPoint.data('total-point');
	var sessionPrice = $('#total-price-answer');//session内の合計金額
	var totalPrice = sessionPrice.data('total-price');
	
	//「ポイントを使用しない」を押した時の挙動
	$('#not-all-use-point').on('click', function() {
		console.log('111111');
		$('#use-point').show();//「ご利用ポイント」の文字
		var nonPoint = $('#not-all-use-point').val();
		outputUsePoint.text(nonPoint.toLocaleString() + 'pt');//ご利用ポイント(0ポイント)
		$('#total-price-answer').text(totalPrice.toLocaleString() + '円');//合計金額
		$('#total-point').text(totalPoint.toLocaleString() + 'pt');//ポイント残高
		$('#use-part-point-text').val('');//「一部のポイントを利用する」のテキストは空欄にする
	});
	
	//「全ポイントを使用する」を押した時の挙動
	$('#all-use-point').on('click', function() {
		console.log('222222');
		$('#use-point').show();
		var totalPriceResult = totalPrice - totalPoint;
		outputUsePoint.text(totalPoint.toLocaleString() + 'pt');//ご利用ポイント(全額ポイント)
		$('#total-price-answer').text(totalPriceResult.toLocaleString() + '円');//合計金額
		$('#total-point').text('0pt');//ポイント残高は0ptになる
		$('#use-part-point-text').val('');//「一部のポイントを利用する」のテキストは空欄にする
	});
	
	//「一部のポイントを利用する」でポイントを入力する際の挙動
	$('#use-part-point-text').on('keyup', function(){
		console.log('333333');
		var input = $(this);//テキストに入力したポイント数
		var value = input.val();//テキストに入力した値を変数に代入
		var answer = totalPoint - value;//ポイント数合計
		var totalPriceResult = totalPrice - value;//合計金額の計算
		$('#use-point').show();
		console.log(value);
		outputUsePoint.text(value.toLocaleString() + 'pt');//ご利用ポイント
		$('#total-price-answer').text(totalPriceResult.toLocaleString() + '円');//合計金額
		$('#total-point').text(answer.toLocaleString() + 'pt');//ポイント残高
	})
});