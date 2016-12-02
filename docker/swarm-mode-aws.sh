#!/bin/bash

set -e


echo "-------------------------"
echo "### Docker for AWS - beta ###"
echo "-------------------------"
echo "Step1:  Register for AWS - beta (https://beta.docker.com/docs/)"
echo "Step2:  Login to your account as a root and create user that will be used latter (http://docs.aws.amazon.com/IAM/latest/UserGuide/id_users_create.html#id_users_create_console)"
echo "Step3:  Create Your Key Pair Using Amazon EC2. Please not that the key will be downloaded by the browser. In my case it is '/Users/idugalic/.ssh/idugalic.pem' file. (http://docs.aws.amazon.com/AWSEC2/latest/UserGuide/ec2-key-pairs.html#having-ec2-create-your-key-pair)"
echo "Step4:  Create stack on AWS by using CloudFormation - Follow the instructions in the email (http://docker.us13.list-manage.com/track/click?u=761fa9756d4209ea04a811254&id=cd3b21e42e&e=78cc80554c)"
echo "-----------------------------------------------------------------------------------------------------------------------------"

echo "Find public DNS of one of your manager nodes (go to EC2 console and explore one manager instance for this)"
echo "#################################################################################################################"
echo -n "Type the public DNS of your manger node, followed by [ENTER]:"
read mangerdns
echo -n "Type the path to your *.pem file, followed by [ENTER] (for example: /Users/idugalic/.ssh/idugalic.pem):"
read keypath

# chmod 400 ~/.ssh/idugalic.pem
chmod 400 "$keypath"
echo "Openining the SSH tunel for $mangerdns"
echo "######################################"
# ssh -NL localhost:2374:/var/run/docker.sock -i ~/.ssh/idugalic.pem docker@ec2-35-156-14-194.eu-central-1.compute.amazonaws.com &
ssh -NL localhost:2374:/var/run/docker.sock -i $keypath $mangerdns &

export DOCKER_HOST=localhost:2374
echo "Deploying the bundle"
echo "####################"

docker stack deploy --compose-file docker-compose-v3.yml micro-company
