'use strict'

/*$(function () {
	//「一部のポイントを利用する」のテキストをクリックすると
  	$('#use-part-point-text').on('click', function(){
		//「一部のポイントを利用する」のラジオボタンにチェックが入る
		$('#use-part-point-radio').prop('checked', true);
	});
	
	
	//以下、ポイント使用時の挙動
	$('#use-point').hide();//「ご利用ポイント」の文字
	$('.total-point-text').show();
	var outputUsePoint = $('.use-point');//実際のご利用ポイント
	var totalPoint = $('#all-use-point').val();//session内のポイント総額
	var totalPrice = $('#total-price-answer').val();//session内の合計金額
	
	//「ポイントを使用しない」を押した時の挙動
	$('#not-all-use-point').on('click', function() {
		console.log('111111');
		var nonPoint = $('#not-all-use-point').val();
		$('#use-point').show();
		outputUsePoint.text(nonPoint + 'pt');
		$('.total-point').text(totalPoint + 'pt');
		$('#use-part-point-text').val('');
	});
	
	//「全ポイントを使用する」を押した時の挙動
	$('#all-use-point').on('click', function() {
		console.log('222222');
		console.log(totalPoint);
		$('#use-point').show();
		Number(totalPoint).toLocaleString();//カンマ区切りに変換
		outputUsePoint.text(totalPoint + 'pt');
		$('.total-point').text('0pt');
		console.log(totalPrice);
		
		$('#use-part-point-text').val('');
	});
	
	//「一部のポイントを利用する」でポイントを入力する際の挙動
	$('#use-part-point-text').on('input', function(){
		console.log('333333');
		var input = $('#use-part-point-text');//テキストに入力したポイント数
		var value = input.val();//テキストに入力した値を変数に代入
		var answer = totalPoint - value;
		console.log(answer);
		$('#use-point').show();
		outputUsePoint.text(value + 'pt');
		Number(answer).toLocaleString();//カンマ区切りに変換
		console.log(totalPrice);
		
		$('.total-point').text(answer + 'pt');
	})
});
*/