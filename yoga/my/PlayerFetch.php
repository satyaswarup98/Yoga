<?php
    $con = mysqli_connect("localhost:3306", "barabati", "admin@#!", "barabati_yoga");
    
	$stage = $_POST["stage_no"];
   
    $statement = mysqli_prepare($con, "SELECT regd_no FROM users_yoga WHERE stage_status=1 AND stage_no=".$stage."");
   	$statement->execute();
	$result = $statement->get_result();

	$response = array();
    $response["success"] = false;
	
	$i=1;
	while ($row = $result->fetch_assoc()) {
		$response["success"] = true;
		$response["player".$i] =$row['regd_no'];
		$i=$i+1;
	}
	$statement->free_result();
	$statement->close();
	
    echo json_encode($response);
?>
