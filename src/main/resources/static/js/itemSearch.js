$(function() {
	
	window.addEventListener('DOMContentLoaded', function(){

		$('input[value="0"]').prop('checked', false);
		
		let animalId = $('#animalId').val();
		console.log(animalId);
		
		switch(animalId){
			case '0':
				$('input[value="0"]').prop('checked', true);
				console.log(animalId);
				break;
			case '1':
				$('input[value="1"]').prop('checked', true);
				console.log(animalId);
				break;
			case '2':
				$('input[value="2"]').prop('checked', true);
				console.log(animalId);
				break;
		}
	})
});

	