#!/bin/bash

set -e


echo "-------------------------"
echo "### Docker for AWS - public beta ###"
echo "-------------------------"
echo "Step1:  Login to your account as a root and create user that will be used latter (http://docs.aws.amazon.com/IAM/latest/UserGuide/id_users_create.html#id_users_create_console)"
echo "Step2:  Create Your Key Pair Using Amazon EC2. Please not that the key will be downloaded by the browser. In my case it is '/Users/idugalic/.ssh/idugalic.pem' file. (http://docs.aws.amazon.com/AWSEC2/latest/UserGuide/ec2-key-pairs.html#having-ec2-create-your-key-pair)"
echo "Step3:  Create stack on AWS by using CloudFormation template - https://blog.docker.com/2016/11/docker-aws-public-beta/ (https://console.aws.amazon.com/cloudformation/home#/stacks/new?stackName=Docker&templateURL=https://docker-for-aws.s3.amazonaws.com/aws/beta/aws-v1.13.0-rc2-beta12.json)"
echo "-----------------------------------------------------------------------------------------------------------------------------"

echo "Find public DNS of one of your AWS manager nodes (go to EC2 console and explore one of the manager instance for this)"
echo "#################################################################################################################"
echo -n "Type the public DNS of your manger node, followed by [ENTER]:"
read mangerdns
echo "Find public DNS of the load balancer (go to EC2 console and find details of the load balancer)"
echo "#################################################################################################################"
echo -n "Type the public DNS of your load balancer, followed by [ENTER]:"
read elbdns
echo -n "Type the path to your *.pem file, followed by [ENTER] (for example: /Users/idugalic/.ssh/idugalic.pem):"
read keypath

# chmod 400 ~/.ssh/idugalic.pem
chmod 400 "$keypath"

echo "Copy the docker compose file to AWS manager node"
echo "######################################"
# scp -i ~/.ssh/idugalic.pem docker-compose-v3.yml docker@ec2-35-156-120-226.eu-central-1.compute.amazonaws.com:~
scp -i $keypath -oStrictHostKeyChecking=no docker-compose-v3.yml docker@$mangerdns:~
echo "SSH to $mangerdns and deploy"
echo "######################################"
# ssh -i ~/.ssh/idugalic.pem -oStrictHostKeyChecking=no docker@ec2-35-156-120-226.eu-central-1.compute.amazonaws.com
ssh -i $keypath -oStrictHostKeyChecking=no docker@$mangerdns "docker stack deploy --compose-file docker-compose-v3.yml micro-company"

echo "Visit in your browser: http://$elbdns:9000/socket/index.html"
echo "Create blogpost from console: $ curl -H "Content-Type: application/json" -X POST -d '{"title":"xyz","rawContent":"xyz","publicSlug": "publicslug","draft": true,"broadcast": true,"category": "ENGINEERING", "publishAt": "2016-12-23T14:30:00+00:00"}' http://$elbdns:9000/command/blog/blogpostcommands"
echo "Query blogposts from console: $ curl http://$elbdns:9000/query/blog/blogposts"

