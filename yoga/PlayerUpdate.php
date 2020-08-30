<?php
   $con = mysqli_connect("localhost", "root", "", "chat");
    
	$stage = $_POST["stage_no"];
    $player1 = $_POST["player1"];
    $player2 = $_POST["player2"];
	$player3 = $_POST["player3"];
	
    
    
    $statement = mysqli_prepare($con, "SELECT count(regd_no) as c, count(DISTINCT(a_group)) as d ,sum(stage_status) as e, max(stage_no) as f FROM users_yoga WHERE regd_no in (".$player1.",".$player2.",".$player3.")");
   	$statement->execute();
	$result = $statement->get_result();

	$response = array();
    $response["success"] = false;
	$response["error"] = false;

	while ($row = $result->fetch_assoc()) {
		if($row['c']==3){
			$response["success"] = true;
			if($row['d']!=1){
				$response["error"] = true;
				$response["msg"] = "All players are not in same age group";
			}
			else if($row['e']!=0){
				$response["error"] = true;
				$response["msg"] = "One or More player are already in stage (active stage status)";
			}
			else if($row['f']!=0){
				$response["error"] = true;
				$response["msg"] = "One or More player are allready paticipated";
			}
			else{				
				$statement0 = mysqli_prepare($con, "SELECT count(*) as a FROM users_yoga WHERE stage_status=1 AND stage_no=".$stage."");
   				$statement0->execute();
				$result = $statement0->get_result();
				while ($row = $result->fetch_assoc()) {
					if($row['a']>=3){
						$response["error"] = true;
						$response["msg"] = "Stage is full Now";
					}
					else{					
						$statement2 = mysqli_prepare($con, "UPDATE users_yoga SET stage_no=".$stage.",stage_status=1 where regd_no in (".$player1.",".$player2.",".$player3.")");
						mysqli_stmt_execute($statement2);
					}
				}
				$statement0->free_result();
				$statement0->close();				
			}
		}
	}
	$statement->free_result();
	$statement->close();
	
    echo json_encode($response);
?>
