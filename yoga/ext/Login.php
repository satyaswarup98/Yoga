<?php
    $con = mysqli_connect("localhost", "root", "", "chat");

     
	$stage = 2;
    $username = "sa";
    $password = "sa";
    
    $statement = mysqli_prepare($con, "SELECT id,judge_type,stage_no FROM login_yoga WHERE username = '".$username."' AND password = '".$password."'");
    mysqli_stmt_execute($statement);    
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $id, $judge_type,$stage_no);
    
	$response = array();
    $response["success"] = false;
	$response["error"] = false;
	$response["msg"] = "";
	
	while(mysqli_stmt_fetch($statement)){
        $response["success"] = true;  
        $response["judge_type"] = $judge_type;
    }

	if($judge_type==1){
		if($stage_no==0){
			$statement2 = mysqli_prepare($con, "UPDATE login_yoga SET stage_no=".$stage." where id = ".$id."");
    		mysqli_stmt_execute($statement2);	
			$response["stage_no"] = $stage;
		}
		else{
			$response["error"] = true;
			$response["msg"] = "Stage No. Aleready Allocated to this User";
		}
	}
	else if($judge_type==2){
		if($stage_no==0){			    
  			$statement3 = mysqli_prepare($con, "SELECT coalesce(max(judge_no)+1,1) as max_stage_no FROM login_yoga WHERE stage_no=".$stage."");
			$statement3->execute();
			$result = $statement3->get_result();
			
		   while ($row = $result->fetch_assoc()) {
				$max_stage_no=$row['max_stage_no'];
		   }
		   $statement3->free_result();
		   $statement3->close();
		
				if($max_stage_no<=3){
					$statement4 = mysqli_prepare($con, "UPDATE login_yoga SET stage_no=".$stage.", judge_no=".$max_stage_no." where id = ".$id."");
					mysqli_stmt_execute($statement4); 
					$response["stage_no"] = $stage;
					$response["judge_no"] = $max_stage_no;
				}
				else{
					$response["error"] = true;
					$response["msg"] = "Already alocated 3 judges to this Stage";
				}			
		}
		else{
			$response["error"] = true;
			$response["msg"] = "Stage No. Aleready Allocated to this User";
		}
	}
	else{
		$response["error"] = true;
		$response["msg"] = "Contact Administrator to set your Judge Type";
	}

    echo json_encode($response);
?>
