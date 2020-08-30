<?php
   $con = mysqli_connect("localhost", "root", "", "test");
    
	$stage = $_POST["stage_no"];
    $username = $_POST["username"];
    $password = $_POST["password"];
    
    $statement = mysqli_prepare($con, "SELECT id,judge_type,stage_no,judge_no FROM login_yoga WHERE username = '".$username."' AND password = '".$password."'");
    mysqli_stmt_execute($statement);    
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $id, $judge_type,$stage_no,$judge_no);
    
	$response = array();
    $response["success"] = false;
	
	
	while(mysqli_stmt_fetch($statement)){
        $response["success"] = true;  
        $response["judge_type"] = $judge_type;
  		$response["error"] = false;
		

	if($judge_type==1){
		if($stage_no==0 || $stage_no==$stage){
			$statement2 = mysqli_prepare($con, "UPDATE login_yoga SET stage_no=".$stage.",judge_no=0 where id = ".$id."");
    		mysqli_stmt_execute($statement2);	
			$response["stage_no"] = $stage;
		}
		else{
			$response["error"] = true;
			$response["msg"] = "Another Stage No is aleready allocated to this User";
		}
	}
	else if($judge_type==2){
		if($stage_no==$stage){
			$response["stage_no"] = $stage;
			$response["judge_no"] = $judge_no;
		}
		else if($stage_no==0){			    
  			$statement3 = mysqli_prepare($con, "SELECT coalesce(max(judge_no)+1,1) as max_stage_no FROM login_yoga WHERE stage_no=".$stage." and judge_type=2");
			$statement3->execute();
			$result = $statement3->get_result();
		   while ($row = $result->fetch_assoc()) {
				$max_stage_no=$row['max_stage_no'];
		   }
		   $statement3->free_result();
		   $statement3->close();
		
				if($max_stage_no<=3 && $stage_no==0){
					$statement4 = mysqli_prepare($con, "UPDATE login_yoga SET stage_no=".$stage.", judge_no=".$max_stage_no." where id = ".$id."");
					mysqli_stmt_execute($statement4); 
					$response["stage_no"] = $stage;
					$response["judge_no"] = $max_stage_no;
				}			
				else{
					$response["error"] = true;
					$response["msg"] = "Already alocated three judges to this Stage";
				}			
		}
		else{
			$response["error"] = true;
			$response["msg"] = "Another Stage No is aleready allocated to this User";
		}
	}
	else{
		$response["error"] = true;
		$response["msg"] = "Judge Type is not set for the User";
	}
	}
    echo json_encode($response);
?>
