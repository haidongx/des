package edu.gsu.hxue.des.system;

import edu.gsu.hxue.des.models.Message;
import edu.gsu.hxue.des.models.Model;

public class DESSystemTest {
    public static void main(String[] args) {
        PingPongDESSystem s = new PingPongDESSystem( null );
        PingPongDESSystem pingPongSys = (PingPongDESSystem) s.clone();
        try {
            pingPongSys.run(60);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class PingPongDESSystem extends DESSystem
    {
        private static final long serialVersionUID = -1586480521760899113L;

        public PingPongDESSystem(Object config)
        {
            super(config);
        }

        @Override
        protected void creatModelsAndCoupling(Object config)
        {
            PingPongModel A = new PingPongModel(this, "A", 3, 3);
            PingPongModel B = new PingPongModel(this, "B", 1, 10);
            PingPongModel C = new PingPongModel(this, "C", 0, 5);
            PingPongModel D = new PingPongModel(this, "D", 0, 4);
            PingPongModel E = new PingPongModel(this, "E", 0, 8);

            A.addBallReceiver(B);
            B.addBallReceiver(C);
            C.addBallReceiver(D);
            D.addBallReceiver(E);
            E.addBallReceiver(A);
        }
    }

    private static class PingPongModel extends Model implements Cloneable
    {
        private static final long serialVersionUID = 3283839855137565556L;

        //States
        private String name;
        private double timeToHoldTheBall;
        private int ballNumber;
        private double holdBallTimer = 0;

        //Coupling info
        private int myReceiverIndex;

        public PingPongModel( DESSystem system, String name, int ballNumber, double timeToHoldTheBall )
        {
            super(system);
            this.name = name;
            this.ballNumber = ballNumber;
            this.timeToHoldTheBall = timeToHoldTheBall;
        }

        public void addBallReceiver( PingPongModel receiver )
        {
            this.myReceiverIndex = receiver.getModelIndex();
        }

        @Override
        public void internalTransit(double delta_time)
        {
            if(this.ballNumber>0) this.holdBallTimer += delta_time;
        }

        @Override
        public void externalTransit(Message message) throws Exception
        {
            this.ballNumber++;
            System.out.println(this.name + " got a Ball at " + this.getTime());
        }

        @Override
        public void generateOutput()
        {
            if(this.ballNumber>0 && this.holdBallTimer>=this.timeToHoldTheBall - 0.000000000001)
            {
                System.out.println(this.name + " sends the ball to " + this.system.getModel(myReceiverIndex) + " at "+ this.getTime());
                this.sendMessage(this.myReceiverIndex, null);
                this.ballNumber--;
                this.holdBallTimer=0;
            }
        }

        @Override
        public double getNextInternalTransitionTime()
        {
            if(this.ballNumber>0)
            {
                return this.timeToHoldTheBall - this.holdBallTimer;
            }
            else
                return Double.POSITIVE_INFINITY;
        }

        public String toString()
        {
            return this.name;
        }

        public Object clone()
        {
            PingPongModel c = (PingPongModel) super.clone();
            c.name = "cloned-"+ this.name;
            return c;
        }
    }
}
