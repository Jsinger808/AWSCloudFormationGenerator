package cloudFormationBuilder;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class WriteToExistingFile {

	static int totalbytes = 0;
	 
	 public static void compileCF(String fileName, String nameVPC, String availabilityZoneSelection, String instanceTypeEC2, String nameEC2, String userHomeFolder, String regionSelection) {
		
		 /*Besides the variables, this text must be in every CF Template regardless of user input.
		 It creates the architecture necessary for an EC2 to connect to the Internet:
		 a VPC, a Subnet, a Routing Table, an Internet Gateway, and a Lambda
		 function so the EC2 can use IPv6.
		 
		 Scaling this template is easy. To add more resources, type in a string to another 
		 CFTemplateArrayList.add, and it'll be included at the bottom of your CF Template.
		 */
		 
		 ArrayList<String> CFTemplateArrayList = new ArrayList<String>();
		
		 CFTemplateArrayList.add("Description: VPC + EC2 Instance(s)\nParameters:\n  LatestAmiId:\n    "
				+ "Description: AMI for Instance (default is latest AmaLinux2)\n    Type: 'AWS::SSM::Parameter::Value<AWS::EC2::Image::Id>'\n    "
				+ "Default: '/aws/service/ami-amazon-linux-latest/amzn2-ami-hvm-x86_64-gp2'\nResources:\n  VPC:\n    Type: AWS::EC2::VPC\n    "
				+ "Properties:\n      CidrBlock: 10.16.0.0/16\n      EnableDnsSupport: true\n      EnableDnsHostnames: true\n      "
				+ "Tags:\n        - Key: Name\n          Value: " + nameVPC + "\n");
		CFTemplateArrayList.add("  IPv6CidrBlock:\n    Type: AWS::EC2::VPCCidrBlock\n    Properties:\n      VpcId: !Ref VPC\n      "
				+ "AmazonProvidedIpv6CidrBlock: true\n  InternetGateway:\n    Type: 'AWS::EC2::InternetGateway'\n    Properties:\n      "
				+ "Tags:\n        - Key: Name\n          Value: " + nameVPC + "-igw" + "\n");
		CFTemplateArrayList.add("  InternetGatewayAttachment:\n    Type: 'AWS::EC2::VPCGatewayAttachment'\n    Properties:\n      VpcId: !Ref VPC\n      "
				+ "InternetGatewayId: !Ref InternetGateway\n  RouteTableWeb: \n    Type: 'AWS::EC2::RouteTable'\n    Properties:\n      "
				+ "VpcId: !Ref VPC\n      Tags:\n        - Key: Name\n          Value: " + nameVPC + "-rt" + "\n");
		CFTemplateArrayList.add("  RouteTableWebDefaultIPv4: \n    Type: 'AWS::EC2::Route'\n    DependsOn: InternetGatewayAttachment\n    Properties:\n      "
				+ "RouteTableId:\n        Ref: RouteTableWeb\n      DestinationCidrBlock: '0.0.0.0/0'\n      GatewayId:\n        Ref: InternetGateway\n  "
				+ "RouteTableWebDefaultIPv6: \n    Type: 'AWS::EC2::Route'\n    DependsOn: InternetGatewayAttachment\n    Properties:\n      "
				+ "RouteTableId:\n        Ref: RouteTableWeb\n      DestinationIpv6CidrBlock: '::/0'\n      GatewayId: \n        Ref: InternetGateway\n  "
				+ "RouteTableAssociation:\n    Type: 'AWS::EC2::SubnetRouteTableAssociation'\n    Properties:\n      SubnetId: !Ref Subnet\n      "
				+ "RouteTableId:\n        Ref: RouteTableWeb\n  Subnet:\n    Type: AWS::EC2::Subnet\n    DependsOn: IPv6CidrBlock\n    Properties:\n      "
				+ "VpcId: !Ref VPC\n      AvailabilityZone: !Select [ "  + availabilityZoneSelection +
				", !GetAZs '' ]\n      CidrBlock: 10.16.48.0/20\n      MapPublicIpOnLaunch: true\n      Ipv6CidrBlock: \n        Fn::Sub:\n          "
				+ "- \"${VpcPart}${SubnetPart}\"\n          - SubnetPart: '03::/64'\n            VpcPart: !Select [ 0, !Split [ '00::/56', "
				+ "!Select [ 0, !GetAtt VPC.Ipv6CidrBlocks ]]]\n      Tags:\n        - Key: Name \n          Value: "
				+ nameVPC + "-sn" + "\n");
		CFTemplateArrayList.add("  IPv6WorkaroundSubnet:\n    Type: Custom::SubnetModify\n    Properties:\n      "
				+ "ServiceToken: !GetAtt IPv6WorkaroundLambda.Arn\n      SubnetId: !Ref Subnet\n  IPv6WorkaroundRole:\n    "
				+ "Type: AWS::IAM::Role\n    Properties:\n      AssumeRolePolicyDocument:\n        Version: '2012-10-17'\n        "
				+ "Statement:\n        - Effect: Allow\n          Principal:\n            Service:\n            "
				+ "- lambda.amazonaws.com\n          Action:\n          - sts:AssumeRole\n      Path: \"/\"\n      Policies:\n        "
				+ "- PolicyName: !Sub \"ipv6-fix-logs-${AWS::StackName}\"\n          PolicyDocument:\n            Version: '2012-10-17'\n"
				+ "            Statement:\n            - Effect: Allow\n              Action:\n              - logs:CreateLogGroup\n              "
				+ "- logs:CreateLogStream\n              - logs:PutLogEvents\n              Resource: arn:aws:logs:*:*:*\n        "
				+ "- PolicyName: !Sub \"ipv6-fix-modify-${AWS::StackName}\"\n          PolicyDocument:\n            Version: '2012-10-17'\n"
				+ "            Statement:\n            - Effect: Allow\n              Action:\n              - ec2:ModifySubnetAttribute\n"
				+ "              Resource: \"*\"\n  IPv6WorkaroundLambda:\n    Type: AWS::Lambda::Function\n    Properties:\n      "
				+ "Handler: \"index.lambda_handler\"\n      Code: #import cfnresponse below required to send respose back to CFN\n        "
				+ "ZipFile:\n          Fn::Sub: |\n            import cfnresponse\n            import boto3\n      \n            "
				+ "def lambda_handler(event, context):\n              if event['RequestType'] is 'Delete':\n                "
				+ "cfnresponse.send(event, context, cfnresponse.SUCCESS)\n                return\n      \n              "
				+ "responseValue = event['ResourceProperties']['SubnetId']\n              ec2 = boto3.client('ec2', region_name='${AWS::Region}')\n"
				+ "              ec2.modify_subnet_attribute(AssignIpv6AddressOnCreation={\n                              'Value': True\n"
				+ "                              },\n                              SubnetId=responseValue)\n              responseData = {}\n"
				+ "              responseData['SubnetId'] = responseValue\n              cfnresponse.send(event, context, cfnresponse.SUCCESS, "
				+ "responseData, \"CustomResourcePhysicalID\")\n      Runtime: python3.9\n      Role: !GetAtt IPv6WorkaroundRole.Arn\n      "
				+ "Timeout: 30\n  InstanceSecurityGroup:\n    Type: 'AWS::EC2::SecurityGroup'\n    Properties:\n      VpcId: !Ref VPC\n      "
				+ "GroupDescription: Enable SSH access via port 22 IPv4 & v6\n      SecurityGroupIngress:\n        - Description: "
				+ "'Allow SSH IPv4 IN'\n          IpProtocol: tcp\n          FromPort: '22'\n          ToPort: '22'\n          "
				+ "CidrIp: '0.0.0.0/0'\n        - Description: 'Allow HTTP IPv4 IN'\n          IpProtocol: tcp\n          FromPort: '80'\n"
				+ "          ToPort: '80'\n          CidrIp: '0.0.0.0/0'\n        - Description: 'Allow SSH IPv6 IN'\n          IpProtocol: tcp\n"
				+ "          FromPort: '22'\n          ToPort: '22'\n          CidrIpv6: ::/0\n  PublicEC2:\n    Type: AWS::EC2::Instance\n    "
				+ "Properties:\n      InstanceType: "
				+ instanceTypeEC2 + "\n");
		CFTemplateArrayList.add("      ImageId: !Ref LatestAmiId\n      SubnetId: !Ref Subnet\n      SecurityGroupIds: \n        - !Ref InstanceSecurityGroup\n"
				+ "      Tags:\n        - Key: Name\n          Value: "
				+ nameEC2 + "\n");
		
		
		for (int i = 0; i < CFTemplateArrayList.size(); ++i) {
		writeCFSection(userHomeFolder, fileName, CFTemplateArrayList.get(i));
		}
		
		 System.out.println("Successfully created your CloudFormation Template " + fileName + ".yaml at " + userHomeFolder + 
				 ". Please upload your file on AWS as a CloudFormation stack in your selected region: " + regionSelection + ". Enjoy!");

	 }
	 public static void writeCFSection(String userHomeFolder, String fileName, String CFTemplate) {
		 try {
				RandomAccessFile randomAccessFile = new RandomAccessFile(userHomeFolder + fileName + ".yaml", "rw");
				randomAccessFile.seek(totalbytes);
				byte[] bytes = CFTemplate.getBytes("UTF-8");
				totalbytes = totalbytes + bytes.length;
				randomAccessFile.write(bytes);
				randomAccessFile.close();
				}
				catch (IOException e) {
					 System.out.println("An error occurred.");
				     e.printStackTrace();
				}
	 }
		 
}



