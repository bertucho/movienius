$("document").ready(function(){
	$("#search-box").keypress(searchMovie);
	getUsersTimeline();
});

function getUsersTimeline(){
	$("#message").html("");
	$('main').html("<center>Cargando...</center>");
	$.getJSON("/users", function(data){
		$('main').html("");
		var $usersListElement = createUsersList();
		data.forEach(function(user){
			var isAdmin = user['roles'].some(function(role){
				return role['authority'] == "ROLE_ADMIN";
			});
			var role = isAdmin ? "Admin" : "User";
			var $row = createUserRow(user['name'], user['email'], role);
			$usersListElement.append($row);
		});
		if(window.isMoviniusAdmin) $usersListElement.append(newUserAction());
		$('main').append($usersListElement);
	});
}

// Creates Skeleton for list of users
// <ul id="users"> <Row title of columns> </ul>
function createUsersList(){
	var $element = $(document.createElement('ul'));
	$element.attr("id", "users");
	$element.append(createHeaderRow());
	return $element;
}

function createHeaderRow(){
	var $element = $(document.createElement("div"));
	$element.addClass("user-row");
	$element.append('<div class="username">Username</div>');
	$element.append('<div class="email">Email</div>');
	$element.append('<div class="role">Role</div>');
	$element.children().addClass("col-md-4 col-sm-4 col-xs-4");
	return $element;
}

function createUserRow(username, email, role){
	var $element = $(document.createElement('li'));
	$element.attr("id", username);
	$element.addClass("user-row");
	$element.append('<div class="username">' + username + '</div>');
	$element.append('<div class="email">' + email + '</div>');
	$element.append('<div class="role">' + role + '</div>');
	if (window.isMoviniusAdmin){
		$element.addClass("edit");
		$element.click(editUser);
	}
	$element.children().addClass("col-md-4 col-sm-4 col-xs-4");
	return $element;
}

function getMoviesTimeline(){
	$("#message").html("");
	$('main').html("<center>Cargando...</center>");
	$.get("/movies-html", function(data){
		//$result = $.parseHTML(data);
		$("main").html(data);
	});
}

function searchMovie(event){
	if (event.which == 13){
		var title = $(event.target).val();
		$("main").html("<center>Buscando...</center>");
		$.get("/movies/get/"+title, function(data){
			$("main").html(data);
		});
	}
}