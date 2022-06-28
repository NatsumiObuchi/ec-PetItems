'use strict'

$(function () {
	
	//「一部のポイントを利用する」のテキストをクリックすると
  	$('#use-part-point-text').on('click', function(){
		//「一部のポイントを利用する」のラジオボタンにチェックが入る
		$('#use-part-point-radio').prop('checked', true);
	});
	
	
	//以下、ポイント使用時の挙動
	var outputUsePoint = $('.use-point');//実際のご利用ポイント
	var sessionPoint = $('#total-point');//session内のポイント総額
	var totalPoint = sessionPoint.data('total-point');
	var sessionPrice = $('#total-price-answer');//session内の合計金額
	var totalPrice = sessionPrice.data('total-price');
	
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
		$('#total-price-answer').text(totalPrice.toLocaleString() + '円');//合計金額
		$('#total-point').text(totalPoint+ 'pt');//ポイント残高
		$('#use-part-point-text').val('');//「一部のポイントを利用する」のテキストは空欄にする
	});
	
	//「全ポイントを使用する」を押した時の挙動
	$('#all-use-point').on('click', function() {
		console.log('222222');
		if (totalPrice <= totalPoint) {//合計金額よりもポイント残高の方が大きかった場合
			var totalPointResult = totalPoint - totalPrice;
			outputUsePoint.text(totalPrice.toLocaleString() + 'pt');//ご利用ポイント=今回の合計金額分
			$('#total-price-answer').text('0円');//合計金額
			$('#total-point').text(totalPointResult + 'pt');//ポイント残高
			
		} else {
			var totalPriceResult = totalPrice - totalPoint;
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
		$('#total-price-answer').text(totalPrice.toLocaleString() + '円');//合計金額
		$('#total-point').text(totalPoint.toLocaleString() + 'pt');//ポイント残高
	});
	
	//「一部のポイントを利用する」でポイントを入力する際の挙動
	$('#use-part-point-text').on('keyup', function(){//inputでもいけた
		console.log('4444444');
		var input = $(this);//テキストに入力したポイント数
		var value = input.val();//テキストに入力した値を変数に代入
		var answer = totalPoint - value;//ポイント数合計
		if (totalPoint < value) {
			$('#error-point-message').show();
			alert('ポイント残高を超えての決済は出来ません。\n' + '修正してください。');
			outputUsePoint.text('0pt');//ご利用ポイント
			$('#total-price-answer').text(totalPrice.toLocaleString() + '円');//合計金額
			$('#total-point').text(totalPoint.toLocaleString() + 'pt');//ポイント残高
			$('#use-part-point-text').val('');//「一部のポイントを利用する」のテキストは空欄にする
		} else {
			var totalPriceResult = totalPrice - value;//合計金額の計算
			$('#use-point').show();
			outputUsePoint.text(value.toLocaleString() + 'pt');//ご利用ポイント
			$('#total-price-answer').text(totalPriceResult.toLocaleString() + '円');//合計金額
			$('#total-point').text(answer.toLocaleString() + 'pt');//ポイント残高
		}
	})
});