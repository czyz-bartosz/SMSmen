import express from 'express';
import SmsQueue from './../containers/SmsQueue.js';
import SmsQueueNode from './../containers/SmsQueueNode.js';

const router = new express.Router;
const smsQueue = new SmsQueue();

router.use(express.json());

router.use((err, req, res, next) => {
    //catch json parser errors
    if (err instanceof SyntaxError && err.status === 400 && 'body' in err) {
        return res.status(400).json({message: err.message});
    }

    next();
});

router.route('/sms')
    .get((req, res) => {
        const node = smsQueue.get();
        if(node === null) {
            res.status(204);
        }

        res.json(node);
        smsQueue.remove();
    })
    .post((req, res) => {
        const { phoneNumber = null, message = null } = req.body;
        try {
            smsQueue.add(new SmsQueueNode(phoneNumber, message));
            res.status(201).json();
        }catch(e) {
            res.status(400)
            res.json({
                message: e.message
            });
        }
    })

export default router;