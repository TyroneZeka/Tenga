import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { z } from 'zod'
import { useLocation, useNavigate } from 'react-router-dom'
import { useVerifyOtp } from '@/features/auth'

const schema = z.object({
  otp: z.string().length(6, 'OTP must be 6 digits').regex(/^\d+$/, 'OTP must be digits only'),
})

type FormValues = z.infer<typeof schema>

export default function VerifyOtpPage() {
  const location = useLocation()
  const navigate = useNavigate()
  const phone = (location.state as { phone?: string } | null)?.phone

  const { mutate, isPending, error } = useVerifyOtp()
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<FormValues>({ resolver: zodResolver(schema) })

  if (!phone) {
    navigate('/login', { replace: true })
    return null
  }

  return (
    <div className="flex min-h-screen items-center justify-center bg-gray-50 px-4">
      <div className="w-full max-w-sm rounded-2xl bg-white p-8 shadow-md">
        <h1 className="mb-2 text-center text-2xl font-semibold text-gray-900">Enter OTP</h1>
        <p className="mb-6 text-center text-sm text-gray-500">
          We sent a 6-digit code to <span className="font-medium text-gray-700">{phone}</span>
        </p>

        <form
          onSubmit={handleSubmit((data) => mutate({ phone, otp: data.otp }))}
          className="space-y-4"
        >
          <div>
            <input
              {...register('otp')}
              maxLength={6}
              className="w-full rounded-lg border border-gray-300 px-3 py-3 text-center text-2xl tracking-widest focus:border-primary-500 focus:outline-none focus:ring-1 focus:ring-primary-500"
              placeholder="000000"
              autoComplete="one-time-code"
            />
            {errors.otp && <p className="mt-1 text-center text-xs text-red-600">{errors.otp.message}</p>}
          </div>

          {error && (
            <p className="text-center text-sm text-red-600">
              {(error as { response?: { data?: { message?: string } } }).response?.data?.message ??
                'Invalid or expired OTP. Please try again.'}
            </p>
          )}

          <button
            type="submit"
            disabled={isPending}
            className="w-full rounded-lg bg-primary-500 py-2.5 text-sm font-medium text-white hover:bg-primary-600 disabled:opacity-50"
          >
            {isPending ? 'Verifying…' : 'Verify'}
          </button>
        </form>
      </div>
    </div>
  )
}
