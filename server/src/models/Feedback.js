import mongoose from 'mongoose';

const FeedbackSchema = new mongoose.Schema(
  {
    name: { type: String, required: true, trim: true },
    email: { type: String, required: true, trim: true, lowercase: true },
    rating: { type: Number, required: true, min: 1, max: 5 },
    feedback: { type: String, required: true, trim: true },
  },
  { timestamps: true }
);

export default mongoose.model('Feedback', FeedbackSchema);
