"use strict";

$(function() {
	$('.withdrawal-link').on('click', function(){
		let checkFlg = window.confirm('退会処理を行います\n続行しますか？');
		if (checkFlg) {
			alert('退会処理を完了しました。画面遷移します。');
			return true;
		} else {
			alert('退会処理を中止しました。');
			return false;
		}
	});
});
