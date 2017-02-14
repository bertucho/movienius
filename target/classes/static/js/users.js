$("document").ready(function(){
	getUsersTimeline();
});

function getUsersTimeline(){
	$.getJSON("/users", function(data){
		var $usersListElement = createUsersList();
		data.forEach(function(user){
			var isAdmin = user['roles'].some(function(role){
				return role['authority'] == "ROLE_ADMIN";
			});
			var role = isAdmin ? "Admin" : "User";
			var $row = createUserRow(user['name'], user['email'], role);
			$usersListElement.append($row);
		});
		$('main').append($usersListElement);
	});
}

// Creates Skeleton for list of users
// <ul id="users"> <Row title of columns> </ul>
function createUsersList(){
	var $element = $(document.createElement('ul'));
	$element.attr("id", "users");
	$element.append(createUserRow("Username", "Email", "Roles"));
	return $element;
}

function createUserRow(username, email, role){
	var $element = $(document.createElement('li'));
	$element.attr("id", username);
	$element.addClass("user-row");
	if (window.isMoviniusAdmin) $element.addClass("edit");
	$element.append('<div class="username">' + username + '</div>');
	$element.append('<div class="email">' + email + '</div>');
	$element.append('<div class="role">' + role + '</div>');
	return $element;
}