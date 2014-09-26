<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Client Profile</title>
<link rel="stylesheet" href="css/website.css"/>
<link rel="stylesheet" href="js/jquery-ui-1.11.1.custom/jquery-ui.min.css"/>
<script src="js/jquery-1.11.1.min.js"></script>
<script src="js/jquery-ui-1.11.1.custom/jquery-ui.min.js"></script>
<script>

var username = '<%= session.getAttribute("username")%>';
var isediting = false;

$(function() {
    $('#birthday').datepicker({
        changeYear: true,
        changeMonth: true,
        dateFormat: "yy-mm-dd",
        beforeShow: function(i) { 
            if ($(i).attr('readonly')) { return false; }
        }
    });

    $('input[name="gender"]').click( function () {
        if (!isediting) {
            return false;
        }
    });
});

function showClientProfile() {
	var getClientProfileRequest = $.ajax( {
	    type:'post',
	    url:'ClientProfileServlet?flag='+Math.random(),
	    data: {"username":username},
	    dataType: 'json',
	    success: function(result) {
	        if (result!=null){
	            $('#fname').val(result.fname);
	            $('#mname').val(result.mname);
	            $('#lname').val(result.lname);

	            //$('#gender').val(result.gender);
	            
	            var strGender = result.gender;
	            var rdGenderSel = "input[name='gender'][value='"+strGender+"']";
	            $(rdGenderSel,"#clientProfile").attr("checked",true);

	            $('#birthday').val(result.birthday);
	            $('#tel').val(result.tel);
	            $('#add1').val(result.add1);
	            $('#add2').val(result.add2);
	            $('#zip').val(result.zip);
	            $('#email').val(result.email);
	            $('#username').val(result.username);
	            $('#pw').val(result.pw);
	        } else{
	            return false;
	        }    
	    },
	     
	    error:  function( jqXHR, textStatus,  errorThrown ){
	    	alert( "Cannot get Client Profile: " + textStatus )
		}

	});
}

function beforeEdit() {
	$('#edit').show();
    $('#confirmEdit').hide();
    $('#cancelEdit').hide();
    showClientProfile();

    $('.clientAttr').attr("readonly","readonly");
    isediting = false;
}

function beginEdit() {
	$('#edit').hide();
    $('#confirmEdit').show();
    $('#cancelEdit').show();

    $('.clientAttr').removeAttr("readonly");
    $('#username').attr("readonly","readonly"); //cannot change username

    isediting = true;
}

function confirmEdit() {
	// update datebase
	var fname = $('#fname').val();
    var mname = $('#mname').val();
    var lname = $('#lname').val();
    //var gender = $('#gender').val();
    var gender = $("input[name='gender']:checked","#clientProfile").val();
    var birthday = $('#birthday').val();
    var tel = $('#tel').val();
    var add1 = $('#add1').val();
    var add2 = $('#add2').val();
    var zip = $('#zip').val();
    var email = $('#email').val();
    //var username = $('#username').val();
    var pw = $('#pw').val();
                
	var updateClientProfileRequest = $.ajax( {
	    type: "post",
	    url: "UpdateClientProfileServlet?flag="+Math.random(),
		data: {
			"username":username,
			"fname":fname,
			"mname":mname,
			"lname":lname,
			"gender":gender,
			"birthday":birthday,
			"tel":tel,
			"add1":add1,
			"add2":add2,
			"zip":zip,
			"email":email,
			"pw":pw
		},
		dataType:"json",
		success: function(data){
			if (data != null) {
			    if (data.result=="true"){
				    alert("Client Profile Changed!");
				    return true;
				} else {
					return false;
				}
			} else {
			    return false;
			}
		},
		error: function(jqXHR,textStatus,errorThrown) {
			alert("Cannot apply change to Client Profile.");
		}
	});
	
    beforeEdit();
}

function cancelEdit() {
	beforeEdit();
}


$(document).ready(function() {
	beforeEdit();
	
    $('#edit').click( function() {
    	beginEdit();
    });

    $('#confirmEdit').click( function() {
    	confirmEdit();
    });

    $('#cancelEdit').click( function() {
    	cancelEdit();
    });
});

</script>
</head>
<body>
    <form id="clientProfile">
    <fieldset>
    <legend>Basic</legend>
        <p>First Name:<input id="fname" type="text" class="clientAttr" ></p>
        <p>Middle Name:<input id="mname" type="text" class="clientAttr" ></p>
        <p>Last Name:<input id="lname" type="text" class="clientAttr" ></p>
        <p>
            Gender:
            <label for="genderM">Male</label>
            <input id="genderM" type="radio" class="clientAttr" name="gender" value="M">
            <label for="genderF">Female</label>
            <input id="genderF" type="radio" class="clientAttr" name="gender" value="F">
        </p>
        <p>Birthday:<input id="birthday" type="text" class="clientAttr" ></p>
    </fieldset>
    <fieldset>
    <legend>Contact</legend>
        <p>Tel:<input id="tel" type="text" class="clientAttr" ></p>
        <p>Address 1:<input id="add1" type="text" class="clientAttr" ></p>
        <p>Address 2:<input id="add2" type="text" class="clientAttr" ></p>
        <p>ZIP:<input id="zip" type="text" class="clientAttr" ></p>
        <p>Email:<input id="email" type="text" class="clientAttr" ></p>
    </fieldset>
    <fieldset>
    <legend>Online Bank</legend>
        <p>Username:<input id="username" type="text" class="clientAttr" ></p>
        <p>Password:<input id="pw" type="text" class="clientAttr" ></p>
    </fieldset>
    <p>
        <input id="edit" type="button" value="Edit" />
        <input id="confirmEdit" type="button" value="Confirm" />
        <input id="cancelEdit" type="button" value="Cancel" />
    </p>
    </form>
</body>
</html>