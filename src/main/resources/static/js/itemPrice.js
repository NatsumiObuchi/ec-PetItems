$(function() {	
	//数量変更用にデフォルトの金額を取得
    var defaultPrice = $('#itemPrice').text();
    
    //初期表示用にデフォルトの金額を取得
    var defaultPrice2 = $('#itemPrice').text();
    	//初期表示の金額をカンマ区切りに変更
    	defaultPrice2 = String(defaultPrice2).replace( /(\d)(?=(\d\d\d)+(?!\d))/g, '$1,');
		$('#itemPrice').text(defaultPrice2);
    
	//数量が変更された場合
	$('#selectItem').on('change', function() {
		let num = $('#selectItem').val();
		let newPrice = 0;
		
		switch (num){
			case '1':
				newPrice = defaultPrice * 1;
				
				break;
			case '2':
				newPrice = defaultPrice * 2;
				break;
			case '3':
				newPrice = defaultPrice * 3;
				break;
			case '4':
				newPrice = defaultPrice * 4;
				break;
			case '5':
				newPrice = defaultPrice * 5;
				break;
			case '6':
				newPrice = defaultPrice * 6;
				break;
			case '7':
				newPrice = defaultPrice * 7;
				break;
			case '8':
				newPrice = defaultPrice * 8;
				break;
			case '9':
				newPrice = defaultPrice * 9;
				break;
			case '10':
				newPrice = defaultPrice * 10;
				break;
			case '11':
				newPrice = defaultPrice * 11;
				break;
			case '12':
				newPrice = defaultPrice * 12;
				break;
		};
		
		//変更後の金額をカンマ区切りに変更
		newPrice = String(newPrice).replace( /(\d)(?=(\d\d\d)+(?!\d))/g, '$1,');
		$('#itemPrice').text(newPrice);
	});		
   
});