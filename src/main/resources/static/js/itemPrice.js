$(function() {	
	//数量変更用にデフォルトの金額を取得
    let defaultPrice = $('#itemPrice').text();
    
    //初期表示用にデフォルトの金額を取得
    let defaultPrice2 = $('#itemPrice').text();
    	//初期表示の金額をカンマ区切りに変更
    	defaultPrice2 = String(defaultPrice2).replace( /(\d)(?=(\d\d\d)+(?!\d))/g, '$1,');
		$('#itemPrice').text(defaultPrice2);
	
	$('#selectItem').on('change', function() {
		let num = $('#selectItem').val();
		let newPrice = defaultPrice * num //合計金額
		
		//変更後の金額をカンマ区切りに変更
		newPrice = String(newPrice).replace( /(\d)(?=(\d\d\d)+(?!\d))/g, '$1,');
		$('#itemPrice').text(newPrice);
	});		
   
});