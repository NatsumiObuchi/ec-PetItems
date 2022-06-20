'use strict';

$(function(){
	
	// id:autoCompleteに入っている文字列をListに入れなおす。「,」区切り、「[」、「]」、「 」はreplaceで置き換え（削除）
	var nameList = $('#autoComplete').text();
	nameList = nameList.split(',');
	nameList[0] = nameList[0].replace('[','');
	nameList[nameList.length - 1] = nameList[nameList.length - 1].replace(']','');
	$.each(nameList,function(index){
		nameList[index] = nameList[index].replace(' ','');
	})
	console.log(nameList);
	//nameListをsorceにオートコンプリート	
	$('#code').autocomplete({
		source : nameList
	});
	
});
