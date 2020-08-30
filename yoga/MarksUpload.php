<?php
     $con = mysqli_connect("localhost", "root", "", "chat");
  
	$stage_no = $_POST["stage_no"];
	$judge_no = $_POST["judge_no"];
	$player1 = $_POST["player1"];
    $player2 = $_POST["player2"];
	$player3 = $_POST["player3"];
 	$total_player1 = $_POST["total_player1"];
    $total_player2 = $_POST["total_player2"];
	$total_player3 = $_POST["total_player3"];

	$response = array();
    $response["success"] = false;

  	$statement1 = mysqli_prepare($con, "UPDATE users_yoga SET mark_j".$judge_no."=".$total_player1.",stage_status=0 where regd_no=".$player1." AND stage_status=1 AND stage_no=".$stage_no."");
	mysqli_stmt_execute($statement1);
	$statement2 = mysqli_prepare($con, "UPDATE users_yoga SET mark_j".$judge_no."=".$total_player2.",stage_status=0 where regd_no=".$player2." AND stage_status=1 AND stage_no=".$stage_no."");
	mysqli_stmt_execute($statement2);
	$statement3 = mysqli_prepare($con, "UPDATE users_yoga SET mark_j".$judge_no."=".$total_player3.",stage_status=0 where regd_no=".$player3." AND stage_status=1 AND stage_no=".$stage_no."");
	mysqli_stmt_execute($statement3);

	
	$response["success"] = true;
    echo json_encode($response);
?>
