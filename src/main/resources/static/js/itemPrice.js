$(function() {	
	//デフォルトの金額を取得
    var defaultPrice = $('#itemPrice').text();

	//数量が変更された場合
	$('#selectItem').on('change', function() {
		let num = $('#selectItem').val();
		let newPrice = 0;
		
		switch (num){
			case '1':
				newPrice = defaultPrice * 1;
				$('#itemPrice').text(newPrice);
				break;
			case '2':
				newPrice = defaultPrice * 2;
				$('#itemPrice').text(newPrice);
				break;
			case '3':
				newPrice = defaultPrice * 3;
				$('#itemPrice').text(newPrice);
				break;
			case '4':
				newPrice = defaultPrice * 4;
				$('#itemPrice').text(newPrice);
				break;
			case '5':
				newPrice = defaultPrice * 5;
				$('#itemPrice').text(newPrice);
				break;
			case '6':
				newPrice = defaultPrice * 6;
				$('#itemPrice').text(newPrice);
				break;
			case '7':
				newPrice = defaultPrice * 7;
				$('#itemPrice').text(newPrice);
				break;
			case '8':
				newPrice = defaultPrice * 8;
				$('#itemPrice').text(newPrice);
				break;
			case '9':
				newPrice = defaultPrice * 9;
				$('#itemPrice').text(newPrice);
				break;
			case '10':
				newPrice = defaultPrice * 10;
				$('#itemPrice').text(newPrice);
				break;
			case '11':
				newPrice = defaultPrice * 11;
				$('#itemPrice').text(newPrice);
				break;
			case '12':
				newPrice = defaultPrice * 12;
				$('#itemPrice').text(newPrice);
				break;
		};
		
	});		
   
});
