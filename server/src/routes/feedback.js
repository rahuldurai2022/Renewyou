import { Router } from 'express';
import Joi from 'joi';
import Feedback from '../models/Feedback.js';

const router = Router();

const feedbackSchema = Joi.object({
  name: Joi.string().min(1).max(120).required(),
  email: Joi.string().email().required(),
  rating: Joi.number().integer().min(1).max(5).required(),
  feedback: Joi.string().min(1).max(4000).required(),
});

// Create
router.post('/', async (req, res) => {
  try {
    const value = await feedbackSchema.validateAsync(req.body, { abortEarly: false });
    const created = await Feedback.create(value);
    res.status(201).json(created);
  } catch (err) {
    if (err.isJoi) return res.status(400).json({ error: 'ValidationError', details: err.details });
    res.status(500).json({ error: 'ServerError', message: err.message });
  }
});

// List
router.get('/', async (_req, res) => {
  try {
    const items = await Feedback.find().sort({ createdAt: -1 });
    res.json(items);
  } catch (err) {
    res.status(500).json({ error: 'ServerError', message: err.message });
  }
});

// Update
router.put('/:id', async (req, res) => {
  try {
    const { id } = req.params;
    const value = await feedbackSchema.validateAsync(req.body, { abortEarly: false });
    const updated = await Feedback.findByIdAndUpdate(id, value, { new: true });
    if (!updated) return res.status(404).json({ error: 'NotFound' });
    res.json(updated);
  } catch (err) {
    if (err.isJoi) return res.status(400).json({ error: 'ValidationError', details: err.details });
    res.status(500).json({ error: 'ServerError', message: err.message });
  }
});

// Delete
router.delete('/:id', async (req, res) => {
  try {
    const { id } = req.params;
    const deleted = await Feedback.findByIdAndDelete(id);
    if (!deleted) return res.status(404).json({ error: 'NotFound' });
    res.json({ ok: true });
  } catch (err) {
    res.status(500).json({ error: 'ServerError', message: err.message });
  }
});

export default router;
