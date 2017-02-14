window.isMoviniusAdmin = true;

$("document").ready(function(){
	$(".edit").click(editUser);
});

function editUser(event){
	var $target = $(event.target).closest("li");
	if ($target.hasClass("user-form")) return;
	var username = $(".username", this).text();
	var email = $(".email", this).text();
	var roles = $(".roles", this).text();
	$form = createUserForm(username, email, roles);
	console.log($form);
	$target.removeClass("user-row edit");
	$target.addClass("user-form");
	$target.html($form);
}

function createUserForm(username, email, role){
	var username = username || "";
	var email = email || "";
	var role = role || "";
	var $form = $(document.createElement("form"));
	$form.append(createInputText("username", username, "Enter Username"));
	$form.append(createInputText("password", "", "Enter Password"));
	$form.append(createInputText("email", email, "Enter email"));
	$form.append(createCheckbox("admin", role == "Admin"));
	$form.append(createSubmit());
	$form.append(createDeleteButton());
	$form.children().addClass("form-inline user-row-element");
	$form.children(":lt(3)").addClass("col-md-3");
	$form.children().eq(3).addClass("col-md-1");
	$form.find("button").addClass("col-md-1");
	if (!username && !email && !role){
		$form.attr("action", "/users");
		$form.attr("method", "POST");
	}else{
		$form.attr("action", "/users/"+username);
		$form.attr("method", "PUT");
	}
	$form.addClass("form-group row");
	$form.submit(sendUser);
	return $form;
}

function createInputText(name, value, placeholder){
	var $inputText = $(document.createElement("input"));
	if (name == "password") $inputText.attr("type", "password");
	else $inputText.attr("type", "text");
	$inputText.attr("name", name);
	$inputText.val(value);
	$inputText.attr("placeholder", placeholder);
	$inputText.wrap("<div></div>");
	return $inputText.parent();
}

function createCheckbox(name, checked){
	var $checkbox = $(document.createElement("input"));
	$checkbox.attr("type", "checkbox");
	$checkbox.attr("name", name);
	$checkbox.prop("checked", checked);
	$checkbox.wrap("<label class='checkbox-inline'></label>");
	$checkbox.parent().append("Admin");
	return $checkbox.parent().wrap("<div></div>").parent();
}

function createSubmit(){
	var $submit = $(document.createElement("button"));
	$submit.attr("type", "submit");
	$submit.addClass("btn-success");
	$submit.text("Send");
	return $submit;
}

function createDeleteButton(){
	var $delete = $(document.createElement("button"));
	$delete.addClass("btn-danger delete");
	$delete.text("Delete");
	$delete.click(deleteUser);
	return $delete;
}

function sendUser(event){
	event.preventDefault();
	$.ajax({
		url: $(this).attr("action"),
		method: $(this).attr("method"),
		data:{
			name: $("input[name='username']", this).val(),
			password: $("input[name='password']", this).val(),
			email: $("input[name='email']", this).val(),
			admin: $("input[name='admin']", this).prop("checked")
		},
		success: getUsersTimeline,
		error: function(){
			$("#message").html("Username already exists");
		}
	});
}

function deleteUser(event){
	event.preventDefault();
	$.ajax({
		url: $(this).parent().attr("action"),
		method: "DELETE",
		success: getUsersTimeline,
		error: function(){
			$("message").html("Error deleting user");
		}
	});
}

function newUserAction(){
	$newUserRow = $(document.createElement("li"));
	$newUserRow.attr("id", "new-user-button");
	$newUserButton = $(document.createElement("button"));
	$newUserButton.addClass("btn-primary btn-block").text("Create New User");
	$newUserRow.append($newUserButton);
	$newUserRow.click(newUserForm);
	return $newUserRow;
}

function newUserForm(event){
	$(event.currentTarget).off("click");
	var $target = $(event.target).closest("li");
	var $form = createUserForm();
	$target.append($form);
}

function editMovie(id){
	event.preventDefault();
	console.log("hola");
	var $form = $("#"+id);
	$.ajax({
		url: $form.attr("action"),
		method: $form.attr("method"),
		data:{
			title: $("input[name='title']", $form).val(),
			poster_url: $("input[name='poster_url']", $form).val(),
			year: $("input[name='year']", $form).val(),
			director: $("input[name='director']", $form).val(),
			actors: $("input[name='actors']", $form).val(),
			rating: $("input[name='rating']", $form).val(),
			description: $("input[name='description']", $form).val(),
			movie_url: $("input[name='movie_url']", $form).val()
		},
		success: function(data){
			$.get("/movies/movie-template/"+data.id, function(template){
				$form.closest(".movie-content").html(template);
			});
		},
		error: function(){
			$("#message").html("Username already exists");
		}
	});
}